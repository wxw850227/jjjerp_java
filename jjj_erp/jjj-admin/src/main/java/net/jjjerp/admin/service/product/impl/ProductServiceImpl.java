package net.jjjerp.admin.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import net.jjjerp.common.entity.product.*;
import net.jjjerp.admin.mapper.product.ProductMapper;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.param.product.ProductParam;
import net.jjjerp.admin.service.product.*;
import net.jjjerp.common.util.ProductUtils;
import net.jjjerp.common.vo.product.ProductImageVo;
import net.jjjerp.common.vo.product.ProductSkuVo;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductExtendService productExtendService;
    @Autowired
    private ProductUtils productUtils;
    @Resource
    private ProductMfrsService mfrsService;
    @Resource
    private ProductUnitService unitService;
    @Resource
    private ProductAttributeService productAttributeService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductUnitService productUnitService;

    /**
     * 商品列表
     * @param productPageParam
     * @return
     */
    @Override
    public Map<String, Object> getList(ProductPageParam productPageParam) {
        Map<String,Object> result = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(productPageParam);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Product::getDeleteFlag, 0);
        //名称搜索
        if(StringUtils.isNotEmpty(productPageParam.getName())){
            wrapper.like(Product::getName, productPageParam.getName());
        }
        //商品分类搜索
        if(productPageParam.getCategoryId() != null && productPageParam.getCategoryId() > 0){
            //查询商品分类ID以及所有子分类ID
            List<Integer> subCategoryIds = productCategoryService.getSubCategoryId(productPageParam.getCategoryId());
            wrapper.in(Product::getCategoryId, subCategoryIds);
        }
        // 启用 0-禁用  1-启用
        if(productPageParam.getEnabled() != null && productPageParam.getEnabled() > -1){
            wrapper.eq(Product::getEnabled, productPageParam.getEnabled());
        }
        //商品条码
        if(StringUtils.isNotBlank(productPageParam.getBarCode())){
            wrapper.like(Product::getBarCode, productPageParam.getBarCode());
        }
        //商品编码
        if(StringUtils.isNotBlank(productPageParam.getProductCode())){
            wrapper.like(Product::getProductCode, productPageParam.getProductCode());
        }
        //规格条码
        if(StringUtils.isNotBlank(productPageParam.getExtendBarCode())){
            List<Long> productIds = productExtendService.list(new LambdaQueryWrapper<ProductExtend>()
                    .like(ProductExtend::getBarCode, productPageParam.getExtendBarCode())
            ).stream().map(ProductExtend::getProductId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(productIds)) {
                productIds.add(0l);
            }
            wrapper.in(Product::getProductId, productIds);
        }
        //规格编码
        if(StringUtils.isNotBlank(productPageParam.getProductExtendCode())){
            List<Long> productIds = productExtendService.list(new LambdaQueryWrapper<ProductExtend>()
                    .like(ProductExtend::getProductCode, productPageParam.getProductExtendCode())
            ).stream().map(ProductExtend::getProductId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(productIds)) {
                productIds.add(0l);
            }
            wrapper.in(Product::getProductId, productIds);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        IPage<Product> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(this::transVo);
        result.put("productList", new Paging(resultPage));
        return result;
    }

    public ProductVo transVo(Product bean) {
        ProductVo vo = new ProductVo();
        BeanUtils.copyProperties(bean, vo);
        ProductCategory productCategory = productCategoryService.getById(bean.getCategoryId());
        //商品分类名称
        if(productCategory != null){
            vo.setCategoryName(productCategory.getName());
        }
        ProductMfrs mfrs = mfrsService.getById(bean.getMfrs());
        //品牌名称
        if(mfrs != null){
            vo.setMfrsName(mfrs.getName());
        }
        ProductUnit productUnit = productUnitService.getById(bean.getUnitId());
        //单位名称
        if(productUnit != null){
            vo.setUnitName(productUnit.getName());
        }
        // sku
        vo.setSkuList(productExtendService.getSkuByProductId(bean.getProductId()));
        // image
        List<ProductImageVo> image = productUtils.getListByProductId(bean.getProductId());
        vo.setImage(image);
        if(CollectionUtils.isNotEmpty(image)){
            //商品图片主图
            vo.setImagePath(image.get(0).getFilePath());
        }
        return vo;
    }

    /**
     * 获取新增或修改数据
     * @param productId
     * @return
     */
    @Override
    public Map<String, Object> getBaseData(Long productId) {
        Map<String, Object> result = new HashMap<>();
        if(productId > 0){
            Product product = this.getById(productId);
            // 商品信息
            ProductVo vo = transVo(product);
            result.put("model", vo);
        }
        // 所有分类
        result.put("category", productCategoryService.getAll());
        // 所有品牌
        result.put("mfrs", mfrsService.getAll());
        // 所有单位
        result.put("unit", unitService.getAll());
        // 所有规格
        result.put("attribute", productAttributeService.getAll());
        return result;
    }

    /**
     * 新增商品
     * @param productParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean add(ProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        //编码
        if(StringUtils.isEmpty(product.getProductCode())){
            //获取最大商品编码
            String maxProductCode = productMapper.getMaxProductCode();
            if(StringUtils.isEmpty(maxProductCode) || "0".equals(maxProductCode)) {
                maxProductCode = "1000000";
            }else {
                maxProductCode = Long.parseLong(maxProductCode) + 1 + "";
            }
            //自动生成编码
            product.setProductCode(maxProductCode);
        }else {
            //校验规格编码是否是数字
//            if(!StringUtils.isNumeric(product.getProductCode())){
//                throw new BusinessException("商品编码只能是数字");
//            }
            //检查编码是否重复
            if(checkIsNameExist(null, product.getProductCode()) > 0){
                throw new BusinessException(product.getProductCode() + "商品编码已存在");
            }
        }
        //启用 0-禁用  1-启用
        product.setEnabled(1);
        // 保存商品
        this.save(product);
        // 保存商品规格
        productParam.setProductId(product.getProductId());
        this.addProductSpec(productParam, false);
        // 保存商品图片
        this.addProductImages(productParam, product);
        return true;
    }

    public int checkIsNameExist(Long id, String productCode) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Product::getProductId, id);
        }
        wrapper.eq(Product::getProductCode, productCode);
        //删除标记，0未删除，1删除
        wrapper.eq(Product::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean edit(ProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        if(StringUtils.isEmpty(product.getProductCode())){
            throw new BusinessException("商品编码不能为空");
        }
        //校验商品编码是否是数字
//        if(!StringUtils.isNumeric(product.getProductCode())){
//            throw new BusinessException("商品编码只能是数字");
//        }
        //检查编码是否重复
        if(checkIsNameExist(product.getProductId(), product.getProductCode()) > 0){
            throw new BusinessException(product.getProductCode() + "商品编码已存在");
        }
        // 保存商品
        this.updateById(product);
        // 保存商品规格
        productParam.setProductId(product.getProductId());
        this.addProductSpec(productParam, true);
        // 保存商品图片
        this.addProductImages(productParam, product);
        return true;
    }

    /**
     * 添加商品规格
     * @param productParam
     * @param isUpdate
     * @return
     */
    private void addProductSpec(ProductParam productParam, boolean isUpdate)
    {
        List<ProductExtend> sku = productParam.getSkuList();
        if(CollectionUtils.isEmpty(sku)){
            if(isUpdate){
                throw new BusinessException("编辑模式规格不能为空");
            }
            //新增时,没加规格默认添加一条规格数据
            ProductExtend extend = new ProductExtend();
            extend.setProductId(productParam.getProductId());
            extend.setProductCode(getMaxProductExtendCode());
            productExtendService.save(extend);
            return;
        }
        for(ProductExtend item : sku){
            item.setProductId(productParam.getProductId());
            if(StringUtils.isEmpty(item.getProductCode())){
                throw new BusinessException("规格编码不能为空");
            }
            //校验规格编码是否是数字
            if(!StringUtils.isNumeric(item.getProductCode())){
                throw new BusinessException("规格编码只能是数字");
            }
            //校验规格编码是否重复
            if(checkIsProductCodeExist(item.getProductExtendId(),item.getProductCode()) > 0){
                throw new BusinessException(item.getProductCode() + "规格编码已存在");
            }
        }
        // 更新模式
        if(isUpdate){
            for(ProductExtend extend : sku){
                if(extend.getProductExtendId() != null && extend.getProductExtendId() > 0){
                    //修改
                    productExtendService.updateById(extend);
                }else {
                    //新增
                    productExtendService.save(extend);
                }
            }
        }else {
            //新增模式
            productExtendService.saveBatch(sku);
        }
        for(ProductExtend item : sku){
            //校验规格编码是否重复
            if(checkIsProductCodeExist(item.getProductExtendId(),item.getProductCode()) > 0){
                throw new BusinessException(item.getProductCode() + "规格编码已存在");
            }
        }
    }

    public int checkIsProductCodeExist(Long id, String productCode) {
        LambdaQueryWrapper<ProductExtend> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(ProductExtend::getProductExtendId, id);
        }
        wrapper.eq(ProductExtend::getProductCode, productCode);
        //删除标记，0未删除，1删除
        wrapper.eq(ProductExtend::getDeleteFlag, 0);
        return productExtendService.count(wrapper);
    }

    /**
     * 添加商品图片
     * imageType 0商品主图，1详情图
     * @param productParam
     * @param product
     * @return
     */
    private void addProductImages(ProductParam productParam, Product product)
    {
        // 先删除图片
        productImageService.remove(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, product.getProductId())
               );
        List<ProductImage> list = productParam.getImage();
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(item -> item.setProductId(product.getProductId()));
            //再新增
            productImageService.saveBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setState(String productId, Integer state) {
        List<Long> productIds =  Arrays.stream(productId.split(",")).map(Long::parseLong).collect(Collectors.toList());
        for(Long id : productIds){
            Product product = new Product();
            product.setProductId(id);
            //启用 0-禁用  1-启用
            product.setEnabled(state);
            if(product.getEnabled() == 0){
                //库存为0才能删除
                if(getProductStock(id) > 0){
                    throw new BusinessException("商品库存大于0，不允许停用");
                }
            }
            this.updateById(product);
        }
        return true;
    }

    //获取商品总库存
    public Long getProductStock(Long productId){
        List<ProductExtend> sku = productExtendService.getSkuByProductId(productId);
        return sku.stream().mapToLong(ProductExtend::getStockNum).sum();
    }

    @Override
    public boolean setDelete(Long productId) {
        Product product = this.getById(productId);
        //只有停用商品才能删除
        if(product.getEnabled() == 1){
            throw new BusinessException("启用状态商品不能删除");
        }
        //删除标记，0未删除，1删除
        product.setDeleteFlag(1);
        return this.updateById(product);
    }

    /**
     * 获取最大规格编码
     * @return
     */
    @Override
    public String getMaxProductExtendCode() {
        //获取最大规格编码
        String maxProductCode = productMapper.getMaxProductExtendCode();
        if(StringUtils.isEmpty(maxProductCode) || "0".equals(maxProductCode)) {
            maxProductCode = "10000";
        }else {
            maxProductCode = Long.parseLong(maxProductCode) + 1 + "";
        }
        return maxProductCode;
    }
}
