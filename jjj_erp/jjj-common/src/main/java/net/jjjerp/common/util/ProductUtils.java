package net.jjjerp.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.entity.product.ProductCategory;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.product.ProductImage;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.common.service.bill.BillItemService;
import net.jjjerp.common.service.depot.DepotStorageService;
import net.jjjerp.common.service.depot.DepotStorageStockService;
import net.jjjerp.common.service.product.ProductCategoryService;
import net.jjjerp.common.service.product.ProductExtendService;
import net.jjjerp.common.service.product.ProductImageService;
import net.jjjerp.common.service.product.ProductMfrsService;
import net.jjjerp.common.vo.product.ProductImageVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductUtils {

    @Autowired
    private ProductExtendService productExtendService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private DepotStorageStockService storageStockService;
    @Autowired
    private DepotStorageStockService depotStorageStockService;
    @Autowired
    private BillItemService billItemService;
    @Autowired
    private DepotStorageService depotStorageService;
    @Autowired
    private ProductMfrsService mfrsService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 库存到货,累积库存
     */
    public void addStock(ProductExtend extend, BillItem item) {
        Long num = item.getBasicNumber();
        //调拨入库单不影响总库存
        if(!BillTypeEnum.DBRKD.getValue().equals(item.getBillType())){
            //当前可用库存
            extend.setStockNum(extend.getStockNum() + num);
            //实际总库存
            extend.setTotalStock(extend.getTotalStock() + num);
            //更新成本价
            extend.setCostPrice(item.getDiscountPrice());
            //期初库存单
            if(BillTypeEnum.QCKCD.getValue().equals(item.getBillType())){
                //期初库存
                extend.setOpenningStock(num);
            }
            //是否可以修改期初成本价,0否,1是
            extend.setInitialStatus(0);
            productExtendService.updateById(extend);
        }
        //商品库存余量
        item.setStockNum(getExtendStock(extend.getProductExtendId(), item.getDepotId()) + num);
        //库存变化数量
        item.setDifferenceNum(num);
        billItemService.updateById(item);
        //获取商品绑定货位
        DepotStorage storage = this.getStorage(extend.getProductExtendId());
        //如果是本仓库货位,则新增货位库存
        if(storage != null && storage.getDepotId().equals(item.getDepotId())){
            DepotStorageStock stock = storage.getStock();
            stock.setStockNum(stock.getStockNum() + num);
            //更新货位库存
            depotStorageStockService.updateById(stock);
        }
        //如果采购入库单业务类型是2订单采购入库,则更新关联的采购订单入库数量
        if(item.getLinkItemId() != null && item.getLinkItemId() > 0 && BusinessTypeEnum.DDCGRK.getValue().equals(item.getBusinessType())){
            //关联采购订单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(billItem != null){
                //可入库数量
                billItem.setAvailableNumber(billItem.getAvailableNumber() - item.getOperNumber());
                //可入库总数量
                billItem.setAvailableAllNumber(billItem.getAvailableAllNumber() - num);
                billItemService.updateById(billItem);
            }
        }
        //如果是调拨入库单,则更新关联的库存调拨单数量
        if(item.getLinkItemId() != null && item.getLinkItemId() > 0 && BillTypeEnum.DBRKD.getValue().equals(item.getBillType())){
            //关联库存调拨单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(billItem != null){
                //调入数量
                billItem.setFoldNumber(billItem.getFoldNumber() + item.getOperNumber());
                //调入总数
                billItem.setFoldAllNumber(billItem.getFoldAllNumber() + num);
                //待入库数量
                billItem.setNoArrivalNumber(billItem.getNoArrivalNumber() - item.getOperNumber());
                //待入库总数量
                billItem.setNoArrivalAllNumber(billItem.getNoArrivalAllNumber() - num);
                billItemService.updateById(billItem);
            }
        }
        //如果是销售退货单,则更新关联的售后单入货数量
        if(item.getLinkItemId() != null && item.getLinkItemId() > 0 && item.getBillType() != null && BillTypeEnum.XSTHD.getValue().equals(item.getBillType())){
            //关联售后单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(billItem != null){
                //已退数量
                billItem.setReturnNumber(billItem.getReturnNumber() + item.getOperNumber());
                //已退总数
                billItem.setReturnAllNumber(billItem.getReturnAllNumber() + num);
                //可退货总数
                billItem.setAvailableAllNumber(billItem.getAvailableAllNumber() - num);
                billItemService.updateById(billItem);
            }
        }
    }

    //获取商品绑定货位
    private DepotStorage getStorage(Long productExtendId) {
        LambdaQueryWrapper<DepotStorageStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DepotStorageStock::getProductExtendId, productExtendId);
        List<DepotStorageStock> list = depotStorageStockService.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            DepotStorageStock stock = list.get(0);
            DepotStorage storage = depotStorageService.getById(stock.getStorageId());
            if(storage != null){
                storage.setStock(stock);
                return storage;
            }
        }
        return null;
    }

    /**
     * 减少库存
     */
    public void subtractStock(ProductExtend extend, BillItem item) {
        Long num = item.getBasicNumber();
        if(!BillTypeEnum.DBCKD.getValue().equals(item.getBillType())){
            //当前可用库存
            extend.setStockNum(extend.getStockNum() - num);
            //实际总库存
            extend.setTotalStock(extend.getTotalStock() - num);
            //是否可以修改期初成本价,0否,1是
            extend.setInitialStatus(0);
            productExtendService.updateById(extend);
        }
        //商品库存余量
        item.setStockNum(getExtendStock(extend.getProductExtendId(), item.getDepotId()) - num);
        //库存变化数量
        item.setDifferenceNum(item.getDifferenceNum() - num);
        billItemService.updateById(item);
        //获取商品绑定货位
        DepotStorage storage = this.getStorage(extend.getProductExtendId());
        //如果是本仓库货位,则新增货位库存
        if(storage != null && storage.getDepotId().equals(item.getDepotId())){
            DepotStorageStock stock = storage.getStock();
            stock.setStockNum(stock.getStockNum() - num);
            //更新货位库存
            depotStorageStockService.updateById(stock);
        }

        //如果采购退货单业务类型是4采购入库单退货,则更新关联的采购入库单退货数量
        if(item.getLinkItemId() != null && item.getLinkItemId() > 0 && item.getBusinessType() != null && BusinessTypeEnum.CGRKDTH.getValue().equals(item.getBusinessType())){
            //关联采购入库单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(billItem != null){
                //已退数量
                billItem.setReturnNumber(billItem.getReturnNumber() + item.getOperNumber());
                //已退总数
                billItem.setReturnAllNumber(billItem.getReturnAllNumber() + num);
                //可退货总数
                billItem.setAvailableAllNumber(billItem.getAvailableAllNumber() - num);
                billItemService.updateById(billItem);
            }
        }
        //如果是11调拨出库单,则解锁关联的锁定库存
        if(item.getLinkItemId() != null && item.getLinkItemId() > 0 && BillTypeEnum.DBCKD.getValue().equals(item.getBillType())){
            //关联库存调拨单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(billItem != null){
                //调出数量
                billItem.setCalloutNumber(billItem.getFoldNumber() + item.getOperNumber());
                //调出总数
                billItem.setCalloutAllNumber(billItem.getFoldAllNumber() + num);
                //待入库数量
                billItem.setNoArrivalNumber(billItem.getNoArrivalNumber() + item.getOperNumber());
                //待入库总数量
                billItem.setNoArrivalAllNumber(billItem.getNoArrivalAllNumber() +  num);
                //待出库数量
                billItem.setAvailableNumber(billItem.getNoArrivalNumber() - item.getOperNumber());
                //待出库总数量
                billItem.setAvailableAllNumber(billItem.getNoArrivalAllNumber() -  num);
                //已出库,锁定库存减少
                billItem.setFreezeStock(billItem.getFreezeStock() - item.getBasicNumber());
                billItemService.updateById(billItem);
            }
        }
    }

    //获取商品规格实际库存余量
    public Long getExtendStock(Long extendId, Long depotId) {
        //获取出库入库类型子单据
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        //仓库id
        if(depotId != null){
            wrapper.eq(BillItem::getDepotId, depotId);
        }
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        wrapper.eq(BillItem::getProductExtendId, extendId);
        //库存变化数量
        wrapper.ne(BillItem::getDifferenceNum, 0);
        List<BillItem> itemStockList = billItemService.list(wrapper);
        //实际总库存
        Long totalStock = 0l;
        totalStock = totalStock + itemStockList.stream().mapToLong(BillItem::getDifferenceNum).sum();
        return totalStock;
    }

    /**
     * 新增销售订单、库存调拨单 待出库
     * 锁定库存
     */
    public Boolean freezeStock(ProductExtend extend, Long num) {
        if(extend != null && num != null){
            //当前可用库存
            extend.setStockNum(extend.getStockNum() - num);
            //锁定库存
            extend.setFreezeStock(extend.getFreezeStock() + num);
            return productExtendService.updateById(extend);
        }
        return false;
    }

    /**
     * 销售订单、库存调拨单 已出库
     * 解锁库存
     */
    public Boolean backFreezeStock(ProductExtend extend, Long num) {
        if(extend != null && num != null){
            //当前可用库存
            extend.setStockNum(extend.getStockNum() + num);
            //锁定库存
            extend.setFreezeStock(extend.getFreezeStock() - num);
            return productExtendService.updateById(extend);
        }
        return false;
    }

    /**
     * 根据id和图片类型获取图片
     * @param productId  商品id
     * @return
     */
    public List<ProductImageVo> getListByProductId(Long productId){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId)
                .orderByAsc(ProductImage::getId));
        return imageList.stream().map(e -> {
            ProductImageVo productImageVo = new ProductImageVo();
            BeanUtils.copyProperties(e, productImageVo);
            productImageVo.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return productImageVo;
        }).collect(Collectors.toList());
    }

    //查询商品品牌名称
    public String getMfrsName(Integer id) {
        ProductMfrs mfrs = mfrsService.getById(id);
        if(mfrs != null){
            return mfrs.getName();
        }
        return "";
    }

    //查询商品分类名称
    public String getCategoryName(Integer categoryId) {
        ProductCategory category = productCategoryService.getById(categoryId);
        if(category != null){
            return category.getName();
        }
        return "";
    }
}
