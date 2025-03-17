package net.jjjerp.admin.service.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.product.*;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.mapper.product.ProductExtendMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.bill.BillItemService;
import net.jjjerp.admin.service.depot.DepotService;
import net.jjjerp.admin.service.depot.DepotStorageService;
import net.jjjerp.admin.service.depot.DepotStorageStockService;
import net.jjjerp.admin.service.product.ProductCategoryService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.admin.service.product.ProductMfrsService;
import net.jjjerp.admin.service.product.ProductUnitService;
import net.jjjerp.admin.service.product.impl.ProductServiceImpl;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.common.util.ProductUtils;
import net.jjjerp.common.vo.product.ProductImageVo;
import net.jjjerp.common.vo.product.ProductListVo;
import net.jjjerp.common.vo.product.ProductSkuVo;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品价格扩展 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductExtendServiceImpl extends BaseServiceImpl<ProductExtendMapper, ProductExtend> implements ProductExtendService {

    @Autowired
    private ProductExtendMapper productExtendMapper;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductUnitService productUnitService;
    @Autowired
    private DepotStorageStockService storageStockService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ProductMfrsService productMfrsService;
    @Autowired
    private BillHeadService billHeadService;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 根据商品id查询sku
     * @param productId
     * @return
     */
    @Override
    public List<ProductExtend> getSkuByProductId(Long productId) {
        LambdaQueryWrapper<ProductExtend> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除"
        wrapper.eq(ProductExtend::getDeleteFlag, 0);
        wrapper.eq(ProductExtend::getProductId, productId);
        wrapper.orderByDesc(ProductExtend::getCreateTime);
        List<ProductExtend> list = this.list(wrapper);
        return list;
    }

    @Override
    public Paging<ProductExtend> getList(ProductPageParam productPageParam) {
        // 商品列表
        Page<ProductExtend> page = new PageInfo<>(productPageParam);
        LambdaQueryWrapper<ProductExtend> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(ProductExtend::getDeleteFlag, 0);
        //规格条码
        if(StringUtils.isNotEmpty(productPageParam.getBarCode())){
            wrapper.like(ProductExtend::getBarCode, productPageParam.getBarCode());
        }
        //规格编码
        if(StringUtils.isNotEmpty(productPageParam.getProductCode())){
            LambdaQueryWrapper<Product> wrapperProduct = new LambdaQueryWrapper<>();
            wrapperProduct.like(Product::getName, productPageParam.getProductCode());
            List<Long> productIds = productService.list(wrapperProduct).stream().map(Product::getProductId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(productIds)){
                productIds.add(0L);
            }
            wrapper.and(i -> i.or().in(ProductExtend::getProductId, productIds)
                    .or().like(ProductExtend::getProductCode, productPageParam.getProductCode()));
        }
        LambdaQueryWrapper<Product> wrapperProduct = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapperProduct.eq(Product::getDeleteFlag, 0);
        // 启用 0-禁用  1-启用
        wrapperProduct.eq(Product::getEnabled, 1);
        //商品分类搜索
        if(productPageParam.getCategoryId() != null && productPageParam.getCategoryId() > 0){
            //查询商品分类ID以及所有子分类ID
            List<Integer> subCategoryIds = productCategoryService.getSubCategoryId(productPageParam.getCategoryId());
            wrapperProduct.in(Product::getCategoryId, subCategoryIds);
        }
        List<Long> productIds = productService.list(wrapperProduct).stream().map(Product::getProductId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(productIds)){
            productIds.add(0L);
        }
        wrapper.in(ProductExtend::getProductId, productIds);
        wrapper.orderByDesc(ProductExtend::getCreateTime);
        IPage<ProductExtend> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductExtend> resultPage = iPage.convert(this::transVo);
        return new Paging(resultPage);
    }

    //库存状况
    @Override
    public Map<String, Object> conditionIndex(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        // 商品列表
        Page<ProductExtend> page = new PageInfo<>(param);
        LambdaQueryWrapper<ProductExtend> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(ProductExtend::getDeleteFlag, 0);
        //规格条码
        if(StringUtils.isNotEmpty(param.getExtendBarCode())){
            wrapper.like(ProductExtend::getBarCode, param.getExtendBarCode());
        }
        //规格编码
        if(StringUtils.isNotEmpty(param.getProductExtendCode())){
            wrapper.like(ProductExtend::getProductCode, param.getProductExtendCode());
        }
        wrapper.orderByDesc(ProductExtend::getCreateTime);
        IPage<ProductExtend> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductExtend> resultPage = iPage.convert(extend -> this.transStock(extend,param));
        //按totalStock倒序排序
        resultPage.getRecords().stream().sorted(Comparator.comparing(ProductExtend::getTotalStock).reversed()).collect(Collectors.toList());
        map.put("extend",new Paging(resultPage));
        map.put("supplier", supplierService.getAll());
        map.put("depot", depotService.getAll());
        return map;
    }

    //商品库存分布表
    @Override
    public Map<String, Object> distributionIndex(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(param);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Product::getDeleteFlag, 0);
        //商品条码
        if(StringUtils.isNotBlank(param.getBarCode())){
            wrapper.like(Product::getBarCode, param.getBarCode());
        }
        //商品编码
        if(StringUtils.isNotBlank(param.getProductCode())){
            wrapper.like(Product::getProductCode, param.getProductCode());
        }
        //品牌ID
        if(param.getMfrs() != null && param.getMfrs() > 0){
            wrapper.eq(Product::getMfrs, param.getMfrs());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        IPage<Product> iPage = productService.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(extend -> this.transDistribution(extend,param));
        //按totalStock倒序排序
        resultPage.getRecords().stream().sorted(Comparator.comparing(ProductVo::getTotalStock).reversed()).collect(Collectors.toList());
        map.put("product",new Paging(resultPage));
        map.put("supplier", supplierService.getAll());
        map.put("depot", depotService.getAll());
        map.put("mfrs", productMfrsService.getAll());
        return map;
    }

    //期初库存列表
    @Override
    public Map<String, Object> openningIndex(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(param);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Product::getDeleteFlag, 0);
        //商品条码
        if(StringUtils.isNotBlank(param.getBarCode())){
            wrapper.like(Product::getBarCode, param.getBarCode());
        }
        //商品编码
        if(StringUtils.isNotBlank(param.getProductCode())){
            wrapper.like(Product::getProductCode, param.getProductCode());
        }
        //品牌ID
        if(param.getMfrs() != null && param.getMfrs() > 0){
            wrapper.eq(Product::getMfrs, param.getMfrs());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        IPage<Product> iPage = productService.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(extend -> this.transOpenning(extend,param));
        //按期初库存倒序排序
        resultPage.getRecords().stream().sorted(Comparator.comparing(ProductVo::getOpenningStock).reversed()).collect(Collectors.toList());
        map.put("product",new Paging(resultPage));
        map.put("supplier", supplierService.getAll());
        map.put("depot", depotService.getAll());
        map.put("mfrs", productMfrsService.getAll());
        return map;
    }

    //库存盘点列表
    @Override
    public Map<String, Object> checkIndex(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(param);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Product::getDeleteFlag, 0);
        //商品条码
        if(StringUtils.isNotBlank(param.getBarCode())){
            wrapper.like(Product::getBarCode, param.getBarCode());
        }
        //商品编码
        if(StringUtils.isNotBlank(param.getProductCode())){
            wrapper.like(Product::getProductCode, param.getProductCode());
        }
        //品牌ID
        if(param.getMfrs() != null && param.getMfrs() > 0){
            wrapper.eq(Product::getMfrs, param.getMfrs());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        IPage<Product> iPage = productService.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(extend -> this.transCheck(extend,param));
        //按实际总库存倒序排序
        resultPage.getRecords().stream().sorted(Comparator.comparing(ProductVo::getTotalStock).reversed()).collect(Collectors.toList());
        map.put("product",new Paging(resultPage));
        map.put("supplier", supplierService.getAll());
        map.put("depot", depotService.getAll());
        map.put("mfrs", productMfrsService.getAll());
        return map;
    }

    //商品库存盘点转换
    private ProductVo transCheck(Product product, CommonPageParam param) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        //实际总库存
        vo.setTotalStock(0l);
        List<ProductExtend> extendList = this.list(new LambdaQueryWrapper<ProductExtend>()
                .eq(ProductExtend::getProductId, vo.getProductId())
                //删除标记，0未删除，1删除
                .eq(ProductExtend::getDeleteFlag, 0)
        );
        for(ProductExtend extend : extendList){
            param.setProductId(null);
            Long totalStock =  productUtils.getExtendStock(extend.getProductExtendId(), param.getDepotId());
            extend.setTotalStock(totalStock);
            vo.setTotalStock(vo.getTotalStock() + extend.getTotalStock());
        }
        // sku
        vo.setSkuList(extendList);
        // image
        List<ProductImageVo> image = productUtils.getListByProductId(vo.getProductId());
        vo.setImage(image);
        if(CollectionUtils.isNotEmpty(image)){
            //商品图片主图
            vo.setImagePath(image.get(0).getFilePath());
        }
        return vo;
    }

    //商品期初库存转换
    private ProductVo transOpenning(Product product, CommonPageParam param) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        vo.setOpenningStock(0l);
        vo.setCostPrice(BigDecimal.ZERO);
        BigDecimal costPrice = BigDecimal.ZERO;
        List<ProductExtend> extendList = this.list(new LambdaQueryWrapper<ProductExtend>()
                .eq(ProductExtend::getProductId, vo.getProductId())
                //删除标记，0未删除，1删除
                .eq(ProductExtend::getDeleteFlag, 0)
        );
        for(ProductExtend extend : extendList){
            param.setProductId(null);
            extend = this.transExtendOpenning(extend,param);
            vo.setOpenningStock(vo.getOpenningStock() + extend.getOpenningStock());
            costPrice = costPrice.add(extend.getCostPrice());
        }
        if(vo.getOpenningStock() > 0){
            vo.setCostPrice(costPrice.divide(new BigDecimal(vo.getOpenningStock()), 2, RoundingMode.DOWN));
        }
        // sku
        vo.setSkuList(extendList);
        // image
        List<ProductImageVo> image = productUtils.getListByProductId(vo.getProductId());
        vo.setImage(image);
        if(CollectionUtils.isNotEmpty(image)){
            //商品图片主图
            vo.setImagePath(image.get(0).getFilePath());
        }
        return vo;
    }

    //商品规格期初库存转换
    private ProductExtend transExtendOpenning(ProductExtend extend, CommonPageParam param) {
        param.setProductExtendId(extend.getProductExtendId());
        //获取期初库存子单据
        List<BillItem> openningList = billHeadService.getOpenningList(param);
        //期初库存
        Long openningStock = 0l;
        if(CollectionUtils.isNotEmpty(openningList)){
            openningStock = openningList.get(0).getBasicNumber();
        }
        extend.setOpenningStock(openningStock);
        return extend;
    }

    //商品库存分布转换
    private ProductVo transDistribution(Product product, CommonPageParam param) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(product, vo);
        param.setProductId(vo.getProductId());
        //获取出库入库类型子单据
        List<BillItem> itemStockList = billHeadService.getStockitemList(param);
        //获取锁定类型子单据
        List<BillItem> itemFreezeList = billHeadService.getFreezeitemList(param);
        //实际总库存
        Long totalStock = 0l;
        //锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量
        Long freezeStock = 0l;
        totalStock = totalStock + itemStockList.stream().mapToLong(BillItem::getDifferenceNum).sum();
        freezeStock = freezeStock + itemFreezeList.stream().mapToLong(BillItem::getFreezeStock).sum();
        //当前可用库存(实际库存-锁定库存)
        Long stockNum = totalStock - freezeStock;
        vo.setStockNum(stockNum);
        vo.setTotalStock(totalStock);
        vo.setFreezeStock(freezeStock);
        List<ProductExtend> extendList = this.list(new LambdaQueryWrapper<ProductExtend>()
                .eq(ProductExtend::getProductId, product.getProductId())
                //删除标记，0未删除，1删除
                .eq(ProductExtend::getDeleteFlag, 0)
        );
        for(ProductExtend extend : extendList){
            param.setProductId(null);
            extend = this.transStock(extend,param);
        }
        vo.setSkuList(extendList);
        // image
        List<ProductImageVo> image = productUtils.getListByProductId(vo.getProductId());
        vo.setImage(image);
        if(CollectionUtils.isNotEmpty(image)){
            //商品图片主图
            vo.setImagePath(image.get(0).getFilePath());
        }
        return vo;
    }

    //库存状况转换
    public ProductExtend transStock(ProductExtend extend,CommonPageParam param){
        Product product = productService.getById(extend.getProductId());
        if(product != null){
            extend.setName(product.getName());
            //商品编码
            extend.setProductParentCode(product.getProductCode());
            extend.setAccountNum(product.getAccountNum());
            extend.setWeight(product.getWeight());
            extend.setVolume(product.getVolume());
            extend.setStorage(storageStockService.getStorage(extend.getProductExtendId()));
            extend.setMfrsName(productUtils.getMfrsName(product.getMfrs()));
            extend.setCategoryName(productUtils.getCategoryName(product.getCategoryId()));
        }
        param.setProductExtendId(extend.getProductExtendId());
        //获取出库入库类型子单据
        List<BillItem> itemStockList = billHeadService.getStockitemList(param);
        //获取锁定类型子单据
        List<BillItem> itemFreezeList = billHeadService.getFreezeitemList(param);
        //实际总库存
        Long totalStock = 0l;
        //锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量
        Long freezeStock = 0l;
        totalStock = totalStock + itemStockList.stream().mapToLong(BillItem::getDifferenceNum).sum();
        freezeStock = freezeStock + itemFreezeList.stream().mapToLong(BillItem::getFreezeStock).sum();
        //当前可用库存(实际库存-锁定库存)
        Long stockNum = totalStock - freezeStock;
        extend.setStockNum(stockNum);
        extend.setTotalStock(totalStock);
        extend.setFreezeStock(freezeStock);
        return extend;
    }

    public ProductExtend transVo(ProductExtend extend){
        if(extend == null){
            return null;
        }
        Product product = productService.getById(extend.getProductId());
        if(product != null){
            ProductCategory category = productCategoryService.getById(product.getCategoryId());
            if(category != null){
                extend.setCategoryName(category.getName());
            }
            extend.setName(product.getName());
            extend.setAccountNum(product.getAccountNum());
            extend.setStorage(storageStockService.getStorage(extend.getProductExtendId()));
            ProductUnit productUnit = productUnitService.getById(product.getUnitId());
            if(productUnit != null){
                productUnit = productUnitService.transVo(productUnit);
                extend.setProductUnit(productUnit);
            }else {
                extend.setProductUnit(new ProductUnit());
            }
        }
        return extend;
    }
}
