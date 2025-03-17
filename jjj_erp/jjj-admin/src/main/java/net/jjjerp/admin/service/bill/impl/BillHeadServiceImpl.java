package net.jjjerp.admin.service.bill.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.bill.BillAddress;
import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.entity.customer.CustomerAddress;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.finance.capital.Account;
import net.jjjerp.common.entity.finance.capital.AccountMoneyLog;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.settings.Express;
import net.jjjerp.common.entity.shop.AdminUser;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.admin.mapper.bill.BillHeadMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillAddressService;
import net.jjjerp.admin.service.bill.BillDeliveryService;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.bill.BillItemService;
import net.jjjerp.admin.service.customer.CustomerAddressService;
import net.jjjerp.admin.service.customer.CustomerService;
import net.jjjerp.admin.service.depot.DepotService;
import net.jjjerp.admin.service.depot.DepotStorageStockService;
import net.jjjerp.admin.service.finance.capital.AccountMoneyLogService;
import net.jjjerp.admin.service.finance.capital.AccountService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.admin.service.settings.ExpressService;
import net.jjjerp.admin.service.shop.ShopUserService;
import net.jjjerp.admin.service.store.StoreService;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.common.util.CodeUtils;
import net.jjjerp.common.util.OrderUtils;
import net.jjjerp.common.util.ProductUtils;
import net.jjjerp.common.vo.product.ProductStockVo;
import net.jjjerp.common.vo.statement.BacklogVo;
import net.jjjerp.common.vo.statement.DealVo;
import net.jjjerp.common.vo.statement.HomeDealVo;
import net.jjjerp.common.vo.statement.ProductBoardVo;
import net.jjjerp.config.constant.DatePattern;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.util.DateUtil;
import net.jjjerp.framework.util.ShopLoginUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单据主表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Slf4j
@Service
public class BillHeadServiceImpl extends BaseServiceImpl<BillHeadMapper, BillHead> implements BillHeadService {

    @Autowired
    private BillHeadMapper billHeadMapper;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private BillItemService billItemService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private BillDeliveryService billDeliveryService;
    @Autowired
    private ProductExtendService productExtendService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMoneyLogService accountMoneyLogService;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DepotStorageStockService storageStockService;
    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private BillAddressService billAddressService;

    @Override
    public Map<String, Object> indexOrder(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        Page<BillHead> page = new PageInfo(param);
        LambdaQueryWrapper<BillHead> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BillHead::getBillId);
        if(param.getBillType() == null){
            throw new BusinessException("单据类型不能为空");
        }
        wrapper.eq(BillHead::getBillType, param.getBillType());
        if(param.getAuditStatus() != null && param.getAuditStatus() > 0){
            //审核状态，10待审核20通过30拒绝
            wrapper.eq(BillHead::getAuditStatus, param.getAuditStatus());
            if(param.getAuditStatus() == 20 && BillTypeEnum.SHSQD.getValue().equals(param.getBillType())){
                //已审核待处理售后单过滤已完成和已关闭状态
                wrapper.ne(BillHead::getPurchaseStatus, 1);
                wrapper.ne(BillHead::getPurchaseStatus, 3);
            }
        }
        if(param.getPurchaseStatus() != null && param.getPurchaseStatus() >= 0){
            //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
            wrapper.eq(BillHead::getPurchaseStatus, param.getPurchaseStatus());
        }else {
            wrapper.ne(BillHead::getPurchaseStatus, 4);
        }
        if(param.getDepotId() != null && param.getDepotId() > 0){
            //仓库id
            wrapper.eq(BillHead::getDepotId, param.getDepotId());
        }
        if(param.getSupplierId() != null && param.getSupplierId() > 0){
            //供应商id
            wrapper.eq(BillHead::getSupplierId, param.getSupplierId());
        }
        IPage<BillHead> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<BillHead> resultPage = iPage.convert(this::transVo);
        map.put("bill", new Paging<>(resultPage));
        //供应商列表
        map.put("supplier", supplierService.getAll());
        //仓库列表
        map.put("depot", depotService.getAll());
        //业务员列表
        map.put("sales", shopUserService.getSales());
        //店铺列表
        map.put("store", storeService.getAll());
        //物流公司列表
        map.put("express", expressService.getAll());
        //系统用户列表
        map.put("shopUser", shopUserService.getAll());
        return map;
    }

    @Override
    public Map<String, Object> scanIndex(CommonPageParam param) {
        if(StringUtils.isBlank(param.getSearch())){
            throw new BusinessException("请输入搜索条件");
        }
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<BillHead> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BillHead::getCreateTime);
        //审核状态，10待审核20通过30拒绝
        wrapper.eq(BillHead::getAuditStatus, 20);
        if(param.getPurchaseStatus() != null && param.getPurchaseStatus() > 0){
            //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
            wrapper.eq(BillHead::getPurchaseStatus, param.getPurchaseStatus());
        }
        if(BillTypeEnum.XSDD.getValue().equals(param.getBillType())){
            //售后类型(0无售后 10退货退款 20换货 30退款 40补发 50 维修 60 其他)
            wrapper.eq(BillHead::getRefundType, 0);
        }
        LambdaQueryWrapper<BillDelivery> wrapperDelivery = new LambdaQueryWrapper<>();
        wrapperDelivery.eq(BillDelivery::getExpressNo, param.getSearch());
        wrapperDelivery.eq(BillDelivery::getIsDelete, 0);
        List<Long> billIds = billDeliveryService.list(wrapperDelivery).stream()
                .map(BillDelivery::getBillId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(billIds)){
            billIds.add(0L);
        }
        wrapper.in(BillHead::getBillId, billIds);
        List<BillHead> list = this.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            list = list.stream().map(this::transVo).collect(Collectors.toList());
        }
        map.put("bill", list);
        map.put("supplier", supplierService.getAll());
        map.put("depot", depotService.getAll());
        map.put("sales", shopUserService.getSales());
        map.put("store", storeService.getAll());
        map.put("express", expressService.getAll());
        map.put("shopUser", shopUserService.getAll());
        return map;
    }

    //售后处理
    @Override
    public boolean refundManage(BillHead head) {
        BillHead oldHead = this.getById(head.getBillId());
        //单据状态，0未开始、1完成、2进行中,3已关闭,4异常,5待配货,6已发货
        if(head.getPurchaseStatus() == null){
            throw new BusinessException("操作状态不能为空");
        }
        if(head.getPurchaseStatus() != 1 && head.getPurchaseStatus() != 2){
            throw new BusinessException("操作状态只能是处理中或已完成");
        }
        if(StringUtils.isBlank(head.getManageDetails())){
            throw new BusinessException("处理详情不能为空");
        }
        oldHead.setPurchaseStatus(head.getPurchaseStatus());
        oldHead.setManageDetails(head.getManageDetails());
        return this.updateById(oldHead);
    }

    //客户对账单列表
    @Override
    public Paging<BillHead> customerIndex(CommonPageParam param) {
        if(CollectionUtils.isEmpty(param.getTime()) || param.getTime().size() != 2){
            throw new BusinessException("查询日期不能为空");
        }
        Page<BillHead> page = new PageInfo(param);
        LambdaQueryWrapper<BillHead> wrapper = new LambdaQueryWrapper<>();
        //查询时间
        wrapper.between(BillHead::getBusinessTime, param.getTime().get(0) + " 00:00:00", param.getTime().get(1) + " 23:59:59");
        if(param.getCustomerId() == null || param.getCustomerId() <= 0){
            throw new BusinessException("客户不能为空");
        }
        wrapper.eq(BillHead::getCustomerId, param.getCustomerId());
        //单据类型,8销售出库单,18销售退货单
        wrapper.in(BillHead::getBillType, Arrays.asList(8, 18));
        if(StringUtils.isNotBlank(param.getBillTypeStr())){
            //单据类型名称
            wrapper.like(BillHead::getTypeName, param.getBillTypeStr());
        }
        wrapper.orderByDesc(BillHead::getBusinessTime);
        IPage<BillHead> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<BillHead> resultPage = iPage.convert(this::transCustomer);
        return new Paging<>(resultPage);
    }

    //账户汇总表列表
    @Override
    public Map<String, Object> collectIndex(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();

        if(CollectionUtils.isEmpty(param.getTime()) || param.getTime().size() != 2){
            throw new BusinessException("查询日期不能为空");
        }
        Page<Account> page = new PageInfo(param);
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<AccountMoneyLog> wrapperLog = new LambdaQueryWrapper<>();
        //查询时间
        wrapperLog.between(AccountMoneyLog::getBillTime, param.getTime().get(0) + " 00:00:00", param.getTime().get(1) + " 23:59:59");
        wrapperLog.orderByDesc(AccountMoneyLog::getBillTime);
        if(param.getAccountId() != null && param.getAccountId() > 0){
            wrapperLog.eq(AccountMoneyLog::getAccountId,param.getAccountId());
        }
        //账户IDList
        List<Long> accountIdList = accountMoneyLogService.list(wrapperLog).stream().map(AccountMoneyLog::getAccountId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(accountIdList)){
            accountIdList.add(0L);
        }
        wrapper.in(Account::getAccountId,accountIdList);
        wrapper.orderByDesc(Account::getCreateTime);
        IPage<Account> iPage = accountService.page(page, wrapper);
        // 最终返回分页对象
        IPage<Account> resultPage = iPage.convert(this::transCollect);
        map.put("collect", new Paging<>(resultPage));
        map.put("account", accountService.getAll());
        return map;
    }

    //账户汇总转换
    private Account transCollect(Account account) {
        List<AccountMoneyLog> logList = accountMoneyLogService.list(new LambdaQueryWrapper<AccountMoneyLog>()
                .eq(AccountMoneyLog::getAccountId, account.getAccountId())
        );
        //累积本期付款  type收支方向,1支出,2收入
        BigDecimal payment = logList.stream().filter( i -> i.getDirectionType() == 1).map(AccountMoneyLog::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        //累积本期收款
        BigDecimal receipt = logList.stream().filter( i -> i.getDirectionType() == 2).map(AccountMoneyLog::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        //本期变化金额
        BigDecimal change = payment.add(receipt);
        //期末余额
        BigDecimal balance = account.getOpeningMoney().add(change);
        account.setPayment(payment);
        account.setReceipt(receipt);
        account.setBalance(balance);
        account.setChange(change);
        return account;
    }

    //客户对账转换
    private BillHead transCustomer(BillHead head) {
        List<BillItem> billItemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                .eq(BillItem::getBillId, head.getBillId())
                //删除标记，0未删除，1删除
                .eq(BillItem::getIsDelete, 0)
        );
        head.setBillItemList(billItemList);
        if(BillTypeEnum.XSTHD.getValue().equals(head.getBillType())){
            head.setTotalPrice(head.getTotalPrice().negate());
            head.setTotalNum(-head.getTotalNum());
        }
        List<AccountMoneyLog> logList = accountMoneyLogService.list(new LambdaQueryWrapper<AccountMoneyLog>()
                .eq(AccountMoneyLog::getBillId, head.getBillId())
        );
        //累积变动金额
        BigDecimal money = logList.stream().map(AccountMoneyLog::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        //已收金额
        head.setReceivedMoney(money);
        //单据应收款余额
        head.setBalanceMoney(head.getTotalPrice().subtract(head.getReceivedMoney()));
        return head;
    }


    //商品规格库存变化详情
    @Override
    public Map<String, Object> stockIndex(CommonPageParam param) {
        //子单据
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        if(param.getProductExtendId() != null && param.getProductExtendId() > 0){
            wrapper.eq(BillItem::getProductExtendId, param.getProductExtendId());
        }
        Map<String, Object> map = new HashMap<>();
        Page<BillItem> page = new PageInfo(param);

        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            wrapper.in(BillItem::getDepotId, param.getDepotIdList());
        }
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        //查询时间
        if(CollectionUtils.isNotEmpty(param.getTime()) && param.getTime().size() == 2) {
            wrapper.between(BillItem::getCreateTime, param.getTime().get(0), param.getTime().get(1));
        }
        //库存变化数量
        wrapper.ne(BillItem::getDifferenceNum, 0);
        IPage<BillItem> iPage = billItemService.page(page, wrapper);
        // 最终返回分页对象
        IPage<BillItem> resultPage = iPage.convert(this::transItemStock);
        map.put("stock", new Paging<>(resultPage));
        map.put("depot", depotService.getAll());
        map.put("store", storeService.getAll());
        return map;
    }


    //转换库存变化子单
    public BillItem transItemStock(BillItem item){
        item.setHead(transVo(this.getById(item.getBillId())));
        item.setProductExtend(productExtendService.transVo(productExtendService.getById(item.getProductExtendId())));
        return item;
    }


    //商品规格库存变化详情
    @Override
    public List<ProductStockVo> depotIndex(CommonPageParam param) {
        if(param.getProductExtendId() == null){
            throw new BusinessException("商品规格id不能为空");
        }
        List<ProductStockVo> list = new ArrayList<>();
        LambdaQueryWrapper<Depot> depotWrapper = new LambdaQueryWrapper<>();
        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            depotWrapper.in(Depot::getDepotId, param.getDepotIdList());
        }
        //删除标记，0未删除，1删除
        depotWrapper.eq(Depot::getDeleteFlag,0);
        //查询的仓库
        List<Depot> depositList =  depotService.list(depotWrapper);
        for(Depot depot : depositList){
            ProductStockVo productStockVo = new ProductStockVo();
            param.setDepotIdList(Collections.singletonList(depot.getDepotId()));
            //获取出库入库类型子单据
            List<BillItem> itemStockList = this.getStockitemList(param);
            //获取锁定类型子单据
            List<BillItem> itemFreezeList = this.getFreezeitemList(param);
            //实际总库存
            Long totalStock = 0l;
            //锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量
            Long freezeStock = 0l;
            totalStock = totalStock + itemStockList.stream().mapToLong(BillItem::getDifferenceNum).sum();
            freezeStock = freezeStock + itemFreezeList.stream().mapToLong(BillItem::getFreezeStock).sum();
            //当前可用库存(实际库存-锁定库存)
            Long stockNum = totalStock - freezeStock;
            productStockVo.setDepotId(depot.getDepotId());
            productStockVo.setDepotName(depot.getName());
            productStockVo.setStockNum(stockNum);
            productStockVo.setTotalStock(totalStock);
            productStockVo.setFreezeStock(freezeStock);
            list.add(productStockVo);
        }
        return list;
    }

    //获取锁定类型子单据
    public List<BillItem> getFreezeitemList(CommonPageParam param) {
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            //仓库id
            wrapper.in(BillItem::getDepotId, param.getDepotIdList());
        }
        if(param.getSupplierId() != null && param.getSupplierId() > 0){
            //供应商id
            wrapper.eq(BillItem::getSupplierId, param.getSupplierId());
        }
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        if(param.getProductExtendId() != null){
            wrapper.eq(BillItem::getProductExtendId, param.getProductExtendId());
        }
        if(param.getProductId() != null){
            wrapper.eq(BillItem::getProductId, param.getProductId());
        }
        //锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量
        wrapper.ge(BillItem::getFreezeStock, 1);
        return billItemService.list(wrapper);
    }

    //获取期初库存子单据
    public List<BillItem> getOpenningList(CommonPageParam param) {
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            //仓库id
            wrapper.in(BillItem::getDepotId, param.getDepotIdList());
        }
        if(param.getSupplierId() != null && param.getSupplierId() > 0){
            //供应商id
            wrapper.eq(BillItem::getSupplierId, param.getSupplierId());
        }
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        if(param.getProductExtendId() != null){
            wrapper.eq(BillItem::getProductExtendId, param.getProductExtendId());
        }
        if(param.getProductId() != null){
            wrapper.eq(BillItem::getProductId, param.getProductId());
        }
        //期初库存单
        wrapper.eq(BillItem::getBillType, BillTypeEnum.QCKCD.getValue());
        return billItemService.list(wrapper);
    }

    //获取出库入库类型子单据
    public List<BillItem> getStockitemList(CommonPageParam param) {
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            //仓库id
            wrapper.in(BillItem::getDepotId, param.getDepotIdList());
        }
        if(param.getSupplierId() != null && param.getSupplierId() > 0){
            //供应商id
            wrapper.eq(BillItem::getSupplierId, param.getSupplierId());
        }
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        if(param.getProductExtendId() != null){
            wrapper.eq(BillItem::getProductExtendId, param.getProductExtendId());
        }
        if(param.getProductId() != null){
            wrapper.eq(BillItem::getProductId, param.getProductId());
        }
        //库存变化数量
        wrapper.ne(BillItem::getDifferenceNum, 0);
        return billItemService.list(wrapper);
    }

    //查询单据列表
    @Override
    public Map<String, Object> bill(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        Page<BillHead> page = new PageInfo(param);
        LambdaQueryWrapper<BillHead> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BillHead::getCreateTime);
        if(param.getBillType() == null){
            throw new BusinessException("单据类型不能为空");
        }
        //单据类型
        wrapper.eq(BillHead::getBillType, param.getBillType());
        if(CollectionUtils.isNotEmpty(param.getDepotIdList())){
            //仓库id
            wrapper.in(BillHead::getDepotId, param.getDepotIdList());
        }
        if(param.getSupplierId() != null && param.getSupplierId() > 0){
            //仓库id
            wrapper.eq(BillHead::getSupplierId, param.getSupplierId());
        }
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(param.getPurchaseStatus() != null && param.getPurchaseStatus() > 0){
            wrapper.eq(BillHead::getPurchaseStatus, param.getPurchaseStatus());
        }
        if(BillTypeEnum.CGDD.getValue().equals(param.getBillType())){
            //审核状态，10待审核20通过30拒绝
            wrapper.eq(BillHead::getAuditStatus, 20);
            //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
            wrapper.ne(BillHead::getPurchaseStatus, 3);
            List<Long> headIdList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                            //删除标记，0未删除，1删除
                            .eq(BillItem::getIsDelete, 0)
                            //可入库总数量 > 0
                            .ge(BillItem::getAvailableAllNumber, 1))
                    .stream().map(BillItem::getBillId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(headIdList)){
                headIdList.add(0L);
            }
            //待入库数量>0的采购订单
            wrapper.in(BillHead::getBillId, headIdList);
        }
        if(BillTypeEnum.KCDBD.getValue().equals(param.getBillType())){
            //调拨类型,0待出库,1待入库
            if(param.getAllot() != null && param.getAllot() == 0){
                List<BillItem> itemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                        //可出库总数
                        .ge(BillItem::getAvailableAllNumber, 1)
                        //删除标记，0未删除，1删除
                        .eq(BillItem::getIsDelete, 0));
                List<Long> headIdList = itemList.stream().map(BillItem::getBillId).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(headIdList)){
                    headIdList.add(0L);
                }
                //待出库数量>0的库存调拨单
                wrapper.in(BillHead::getBillId, headIdList);
            }
            //调拨类型,0待出库,1待入库
            if(param.getAllot() != null && param.getAllot() == 1){
                List<BillItem> itemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                        //待入库总数量
                        .ge(BillItem::getNoArrivalAllNumber, 1)
                        //删除标记，0未删除，1删除
                        .eq(BillItem::getIsDelete, 0));
                List<Long> headIdList = itemList.stream().map(BillItem::getBillId).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(headIdList)){
                    headIdList.add(0L);
                }
                //待入库数量>0的库存调拨单
                wrapper.in(BillHead::getBillId, headIdList);
            }
        }
        if(BillTypeEnum.XSDD.getValue().equals(param.getBillType()) || BillTypeEnum.CGRKD.getValue().equals(param.getBillType())){
            List<BillItem> itemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                    //可操作数量大于0的子单
                    .ge(BillItem::getAvailableAllNumber, 1)
                    //删除标记，0未删除，1删除
                    .eq(BillItem::getIsDelete, 0));
            //取到货数量大于退货数量的BillItem
            itemList = itemList.stream().filter(item -> item.getOperNumber() > item.getReturnNumber())
                    .collect(Collectors.toList());
            List<Long> headIdList = itemList.stream().map(BillItem::getBillId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(headIdList)){
                headIdList.add(0L);
            }
            //可退货数量>0的采购入库订单
            wrapper.in(BillHead::getBillId, headIdList);
        }

        IPage<BillHead> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<BillHead> resultPage = iPage.convert(this::transVo);
        if(BillTypeEnum.XSDD.getValue().equals(param.getBillType()) || BillTypeEnum.CGRKD.getValue().equals(param.getBillType())){
            //过滤可操作数量大于0的子单
            resultPage = this.filterAvailable(resultPage);
        }
        if(BillTypeEnum.CGDD.getValue().equals(param.getBillType())){
            //过滤可入库总数量大于0的子单
            resultPage = this.filterAvailable(resultPage);
        }
        if(BillTypeEnum.KCDBD.getValue().equals(param.getBillType())){
            //调拨类型,0待出库,1待入库
            if(param.getAllot() != null && param.getAllot() == 0){
                //过滤可操作数量大于0的子单
                resultPage = this.filterAvailable(resultPage);
            }
            //调拨类型,0待出库,1待入库
            if(param.getAllot() != null && param.getAllot() == 1){
                //过滤待入库总数量大于0的子单
                resultPage = this.filterNoAvailable(resultPage);
            }
        }
        map.put("bill", new Paging<>(resultPage));
        //仓库列表
        map.put("depot", depotService.getAll());
        //物流公司列表
        map.put("express", expressService.getAll());
        //店铺列表
        map.put("store", storeService.getAll());
        return map;
    }

    //待入库总数量大于0的子单
    private IPage<BillHead> filterNoAvailable(IPage<BillHead> resultPage) {
        List<BillHead> headList = resultPage.getRecords();
        for(BillHead head : headList){
            //待入库总数量大于0的子单
            head.setBillItemList(head.getBillItemList().stream().filter(item -> item.getNoArrivalAllNumber() > 0)
                    .collect(Collectors.toList()));
        }
        resultPage.setRecords(headList);
        return resultPage;
    }

    //过滤可操作数量大于0的子单
    private IPage<BillHead> filterAvailable(IPage<BillHead> resultPage) {
        List<BillHead> headList = resultPage.getRecords();
        for(BillHead head : headList){
            //可退货总数量大于0的子单
            head.setBillItemList(head.getBillItemList().stream().filter(item -> item.getAvailableAllNumber() > 0)
                    .collect(Collectors.toList()));
        }
        resultPage.setRecords(headList);
        return resultPage;
    }

    public BillHead transVo(BillHead head){
        if(head == null){
            return null;
        }
        if(StringUtils.isNotEmpty(head.getFileUrls())){
            head.setFileList(Arrays.asList(head.getFileUrls().split(",")));
        }
        List<BillItem> billItemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                .eq(BillItem::getBillId, head.getBillId())
                //删除标记，0未删除，1删除
                .eq(BillItem::getIsDelete, 0)
        );
        for(BillItem item : billItemList){
            ProductExtend extend = productExtendService.getById(item.getProductExtendId());
            if(extend != null){
                extend = productExtendService.transVo(extend);
                //获取商品绑定货位
                DepotStorage storage = storageStockService.getStorage(extend.getProductExtendId());
                item.setStorage(storage);
                //规格条码
                item.setBarCode(extend.getBarCode());
            }
            item.setProductExtend(extend);
            item.setCodeNum(head.getCodeNum());
            if(BillTypeEnum.CGDD.getValue().equals(head.getBillType())){
                //可入库总数
                if(item.getAvailableAllNumber() > 0){
                    item.setHint("部分到货");
                }
                if(item.getAvailableAllNumber() >= item.getBasicNumber()){
                    item.setHint("未到货");
                }
                if(item.getAvailableAllNumber() <= 0){
                    item.setHint("完成");
                }
            }
        }
        Integer purchaseStatus = 0;
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(BillTypeEnum.CGDD.getValue().equals(head.getBillType()) && head.getPurchaseStatus() != 3){
            //可入库总数量大于等于实际操作总数量的采购子单
            List<BillItem> NoArrivalList = billItemList.stream().filter(item -> item.getAvailableAllNumber() >= item.getBasicNumber()).collect(Collectors.toList());
            //可入库总数大于0的采购子单
            List<BillItem> AvailableList = billItemList.stream().filter(item -> item.getAvailableAllNumber() > 0).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(AvailableList)){
                //全部入库了
                purchaseStatus = 1;
                head.setHint("已完成");
            }else if (NoArrivalList.size() == billItemList.size()){
                //全部没有入库
                purchaseStatus = 0;
                head.setHint("未到货");
            }else {
                purchaseStatus = 2;
                head.setHint("部分到货");
            }
            if(!purchaseStatus.equals(head.getPurchaseStatus())){
                //单据状态，0未开始、1完成、2进行中,3已关闭,4异常,5待配货,6已发货
                head.setPurchaseStatus(purchaseStatus);
                this.updateById(head);
            }
        }
        head.setBillItemList(billItemList);
        Customer customer = customerService.getById(head.getCustomerId());
        if(customer != null){
            head.setCustomerName(customer.getNickName());
        }
        Account account = accountService.getById(head.getAccountId());
        if(account != null){
            head.setAccountName(account.getAccountName());
        }
        List<BillDelivery> deliveryList = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, head.getBillId())
                //删除标记，0未删除，1删除
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        String expressNo = deliveryList.stream().map(BillDelivery::getExpressNo).collect(Collectors.joining(","));
        head.setExpressNo(expressNo);
        head.setDeliveryList(deliveryList);
        //设置订单状态文本
        head.setStateText(this.getStateTextAttr(head));
        Depot depot = depotService.getById(head.getDepotId());
        if(depot != null){
            //仓库名称
            head.setDepotName(depot.getName());
        }
        Supplier supplier = supplierService.getById(head.getSupplierId());
        if(supplier != null){
            //供应商名称
            head.setSupplierName(supplier.getSupplierName());
        }
        AdminUser sales = shopUserService.getById(head.getSalesId());
        if(sales != null){
            //业务员名称
            head.setSalesName(sales.getUserName());
        }
        AdminUser audit = shopUserService.getById(head.getAuditId());
        if(audit != null){
            //审核员名称
            head.setAuditName(audit.getUserName());
        }
        AdminUser creator = shopUserService.getById(head.getCreatorId());
        if(creator != null){
            //创建人名称
            head.setCreatorName(creator.getUserName());
        }
        if(BillTypeEnum.CGRKD.getValue().equals(head.getBillType())){
            //业务类型,1直接采购入库,2订单采购入库
            if(BusinessTypeEnum.DDCGRK.getValue().equals(head.getBusinessType())){
                head.setBusinessTypeString("订单采购入库");
            }else {
                head.setBusinessTypeString("直接采购入库");
            }
            //结算状态，0待结算、1已结算、2已结清
            head.setSettlementText(head.getSettlementStatus() == 0 ? "待结算" : head.getSettlementStatus() == 1 ? "已结算" : "已结清");
        }
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())){
            LambdaQueryWrapper<BillHead> wrapperRk = new LambdaQueryWrapper<>();
            wrapperRk.orderByDesc(BillHead::getCreateTime);
            wrapperRk.eq(BillHead::getBillType, BillTypeEnum.XSTHD.getValue());
            wrapperRk.apply("FIND_IN_SET({0}, link_bill_num)",head.getCodeNum());
            //关联的已入库单
            head.setRKList(this.list(wrapperRk));
            LambdaQueryWrapper<BillHead> wrapperXd = new LambdaQueryWrapper<>();
            wrapperXd.orderByDesc(BillHead::getCreateTime);
            wrapperXd.eq(BillHead::getBillType, BillTypeEnum.XSDD.getValue());
            wrapperXd.apply("FIND_IN_SET({0}, link_bill_num)",head.getCodeNum());
            //关联的已换货单
            head.setXDList(this.list(wrapperXd));
        }
        BillAddress address =  billAddressService.getOne(new LambdaQueryWrapper<BillAddress>()
                .eq(BillAddress::getBillId, head.getBillId())
        );
        head.setAddress(address==null?new BillAddress():address);
        if(head.getStoreId() != null && head.getStoreId() > 0){
            Store store = storeService.getById(head.getStoreId());
            if(store != null){
                head.setStoreName(store.getStoreName());
            }
        }
        head.setBusinessTimeStr(DateUtil.getDateString(head.getBusinessTime()));
        return head;
    }

    //单据详情
    @Override
    public BillHead billDetail(Long billId) {
        BillHead head = this.getById(billId);
        return this.transVo(head);
    }

    //作业看板
    @Override
    public Map<String, Object> jobBoard(CommonPageParam param) {
        param.setAppId(ShopLoginUtil.getAppId());
        return this.getBaseMapper().jobBoard(param);
    }

    //商品排行
    @Override
    public Map<String, Object> productBoard(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        //热销
        List<ProductBoardVo> hot = this.getBaseMapper().productBoard(param);
        map.put("hot", hot);
        return map;
    }

    //首页数据
    @Override
    public Map<String, Object> getHomeData(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        param.setAppId(ShopLoginUtil.getAppId());
        param.setStartDate(null);
        param.setEndDate(null);
        DealVo sketch = this.getBaseMapper().dealBoard(param);
        //支付金额
        BigDecimal depositMoney = sketch.getDepositMoney();
        //支付客户数
        BigDecimal customerNum = BigDecimal.valueOf(sketch.getCustomerNum());
        //平均客单价
        BigDecimal customerMoney = BigDecimal.ZERO;
        if(depositMoney != null && customerNum.compareTo(BigDecimal.ZERO) > 0){
            customerMoney = depositMoney.divide(customerNum, 2, RoundingMode.DOWN);
        }
        sketch.setCustomerMoney(customerMoney);
        BacklogVo backlog = this.getBaseMapper().backlog(param);
        //数据概况
        map.put("sketch", sketch);
        //销售概况
        map.put("backlog", backlog);
        return map;
    }

    //首页订单趋势
    @Override
    public Map<String, Object> getHomeDeal(CommonPageParam param) {
        //查询报表日期类型,0最近一周,1最近一个月,3本年
        if(param.getDateType() == 3){
            //查询本年
        }else if(StringUtils.isBlank(param.getStartDate()) || StringUtils.isBlank(param.getEndDate())){
            throw new BusinessException("请选择搜索日期");
        }
        param.setAppId(ShopLoginUtil.getAppId());
        Map<String, Object> map = new HashMap<>();
        List<HomeDealVo> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        if(param.getDateType() == 3){
            //年份
            int year = cn.hutool.core.date.DateUtil.year(new Date());
            for (int i = 1; i <= 12; i++) {
                //月份第一天
                String startDate = year + "-" + i + "-" + "01";
                DateTime dateTime = cn.hutool.core.date.DateUtil.parse(startDate,DatePattern.YYYY_MM_DD);
                //月份最后一天
                DateTime endDate = cn.hutool.core.date.DateUtil.endOfMonth(dateTime);//获取月份最后一天（带时分秒）
                param.setStartDate(dateTime.toString());
                param.setEndDate(endDate.toString());
                HomeDealVo dealVo = this.getBaseMapper().dealHome(param);
                dealVo.setDay(year + "-" + i);
                days.add(year + "-" + i);
                data.add(dealVo);
            }
        }else {
            Date startTime = cn.hutool.core.date.DateUtil.parse(param.getStartDate());
            Date endTime = cn.hutool.core.date.DateUtil.parse(param.getEndDate());
            //endTime加一天
            endTime = cn.hutool.core.date.DateUtil.offsetDay(endTime,1);
            for (Date t = startTime; t.before(endTime); t = cn.hutool.core.date.DateUtil.offsetDay(t,1)) {
                String day = cn.hutool.core.date.DateUtil.format(t, "yyyy-MM-dd");
                param.setStartDate(day  + " 00:00:00");
                param.setEndDate(day  + " 23:59:59");
                HomeDealVo dealVo = this.getBaseMapper().dealHome(param);
                dealVo.setDay(day);
                days.add(day);
                data.add(dealVo);
            }
        }
        //订单趋势
        map.put("deal", data);
        //订单趋势时间范围
        map.put("days", days);
        return map;
    }

    //修改收货地址
    @Override
    public boolean editAddress(BillAddress address) {
        if(address.getBillId() == null){
            throw new BusinessException("单据id不能为空");
        }
        BillAddress oldAddress =  billAddressService.getOne(new LambdaQueryWrapper<BillAddress>()
                .eq(BillAddress::getBillId, address.getBillId())
        );
        if(oldAddress == null){
            oldAddress = address;
            billAddressService.save(oldAddress);
        }else {
            BeanUtils.copyProperties(address, oldAddress);
            billAddressService.updateById(oldAddress);
        }
        return true;
    }

    //修改备注信息
    @Override
    public boolean editRemark(BillHead head) {
        if(head.getBillId() == null){
            throw new BusinessException("单据id不能为空");
        }
        BillHead oldHead =  new BillHead();
        oldHead.setBillId(head.getBillId());
        oldHead.setPrintRemark(head.getPrintRemark());
        oldHead.setSystemRemark(head.getSystemRemark());
        //附件List
        if(CollectionUtils.isNotEmpty(head.getFileList())){
            oldHead.setFileUrls(String.join(",", head.getFileList()));
        }
        return this.updateById(oldHead);
    }

    //库存看板
    @Override
    public Map<String, Object> stockBoard(CommonPageParam param) {
        Map<String, Object> map = new HashMap<>();
        //子单据
        LambdaQueryWrapper<BillItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BillItem::getCreateTime);
        wrapper.eq(BillItem::getIsDelete, 0);
        //年份
        int year = cn.hutool.core.date.DateUtil.year(new Date());
        //月份
        int month = cn.hutool.core.date.DateUtil.month(new Date()) + 1;
        //月份第一天
        String start = year + "-" + month + "-" + "01";
        DateTime startDate = cn.hutool.core.date.DateUtil.parse(start,"yyyy-MM-dd");
        //月份最后一天
        DateTime endDate = cn.hutool.core.date.DateUtil.endOfMonth(startDate);//获取月份最后一天（带时分秒）
        //查询时间
        wrapper.between(BillItem::getCreateTime, startDate, endDate);
        //库存变化数量
        wrapper.ne(BillItem::getDifferenceNum, 0);
        //入库数量
        long rkNum = 0L;
        //出库数量
        long ckNum = 0L;
        //动销率
        BigDecimal rate = BigDecimal.ZERO;
        List<BillItem> list =  billItemService.list(wrapper);
        //出库的商品规格
        Set<Long> extendIds = list.stream().map(BillItem::getProductExtendId).collect(Collectors.toSet());
        int num = extendIds.size();
        int count = productExtendService.count(new LambdaQueryWrapper<ProductExtend>()
                //删除标记，0未删除，1删除
                .eq(ProductExtend::getDeleteFlag,0)
        );
        for(BillItem item : list){
            if(item.getDifferenceNum() > 0){
                rkNum = rkNum + item.getDifferenceNum();
            }else {
                ckNum = ckNum - item.getDifferenceNum();
            }
        }
        if(count > 0 && num > 0){
            rate = new BigDecimal(num).divide(new BigDecimal(count),1,RoundingMode.HALF_UP);
        }
        map.put("rkNum", rkNum);
        map.put("ckNum", ckNum);
        map.put("rate", rate);
        return map;
    }

    //交易趋势
    @Override
    public Map<String, Object> dealBoard(CommonPageParam param) {
        //查询报表日期类型,0最近一周,1最近一个月,3本年
        if(param.getDateType() == 3){
            //查询本年
        }else if(StringUtils.isBlank(param.getStartDate()) || StringUtils.isBlank(param.getEndDate())){
            throw new BusinessException("请选择搜索日期");
        }
        param.setAppId(ShopLoginUtil.getAppId());
        Map<String, Object> map = new HashMap<>();
        List<DealVo> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        if(param.getDateType() == 3){
            //年份
            int year = cn.hutool.core.date.DateUtil.year(new Date());
            for (int i = 1; i <= 12; i++) {
                //月份第一天
                String startDate = year + "-" + i + "-" + "01";
                DateTime dateTime = cn.hutool.core.date.DateUtil.parse(startDate,DatePattern.YYYY_MM_DD);
                //月份最后一天
                DateTime endDate = cn.hutool.core.date.DateUtil.endOfMonth(dateTime);//获取月份最后一天（带时分秒）
                param.setStartDate(dateTime.toString());
                param.setEndDate(endDate.toString());
                DealVo dealVo = this.getBaseMapper().dealBoard(param);
                //支付金额
                BigDecimal depositMoney = dealVo.getDepositMoney();
                //支付客户数
                BigDecimal customerNum = BigDecimal.valueOf(dealVo.getCustomerNum());
                //平均客单价
                BigDecimal customerMoney = BigDecimal.ZERO;
                if(depositMoney != null && customerNum.compareTo(BigDecimal.ZERO) > 0){
                    customerMoney = depositMoney.divide(customerNum, 2, RoundingMode.DOWN);
                }
                dealVo.setCustomerMoney(customerMoney);
                dealVo.setDay(year + "-" + i);
                days.add(year + "-" + i);
                data.add(dealVo);
            }
        }else {
            Date startTime = cn.hutool.core.date.DateUtil.parse(param.getStartDate());
            Date endTime = cn.hutool.core.date.DateUtil.parse(param.getEndDate());
            //endTime加一天
            endTime = cn.hutool.core.date.DateUtil.offsetDay(endTime,1);
            for (Date t = startTime; t.before(endTime); t = cn.hutool.core.date.DateUtil.offsetDay(t,1)) {
                String day = cn.hutool.core.date.DateUtil.format(t, "yyyy-MM-dd");
                param.setStartDate(day  + " 00:00:00");
                param.setEndDate(day  + " 23:59:59");
                DealVo dealVo = this.getBaseMapper().dealBoard(param);
                //支付金额
                BigDecimal depositMoney = dealVo.getDepositMoney();
                //支付客户数
                BigDecimal customerNum = BigDecimal.valueOf(dealVo.getCustomerNum());
                //平均客单价
                BigDecimal customerMoney = BigDecimal.ZERO;
                if(depositMoney != null && customerNum.compareTo(BigDecimal.ZERO) > 0){
                    customerMoney = depositMoney.divide(customerNum, 2, RoundingMode.DOWN);
                }
                dealVo.setCustomerMoney(customerMoney);
                dealVo.setDay(day);
                days.add(day);
                data.add(dealVo);
            }
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 订单状态文字描述
     *
     * @param head
     * @return
     */
    private String getStateTextAttr(BillHead head) {
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(head.getPurchaseStatus() == 0){
            return "未开始";
        }
        if(head.getPurchaseStatus() == 1){
            return "完成";
        }
        if(head.getPurchaseStatus() == 2){
            return "进行中";
        }
        if(head.getPurchaseStatus() == 3){
            return "已关闭";
        }
        return "进行中";
    }

    //确认发货
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deliver(Long billId) {
        BillHead oldHead = this.getById(billId);
        BillDelivery oldDelivery = null;
        List<BillDelivery> list = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, billId)
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        //配送方式(10快递发货 20上门自提 30无需物流 40多包裹)
        if(oldHead.getDeliveryType() != 30 && CollectionUtils.isEmpty(list)){
            throw new BusinessException("请先添加物流信息");
        }
        if(oldHead.getDeliveryType() != 30){
            oldDelivery = list.get(0);
            if(StringUtils.isBlank(oldDelivery.getExpressNo())){
                throw new BusinessException("请填写快递单号");
            }
            if(oldDelivery.getExpressId() == null){
                throw new BusinessException("请选择快递公司");
            }
        }
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(6);
        BillHead head = new BillHead();
        BeanUtils.copyProperties(oldHead, head);
        head.setCodeNum(null);
        head.setBillType(BillTypeEnum.XSCKD.getValue());
        List<BillItem> billItemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                .eq(BillItem::getBillId, oldHead.getBillId())
                //删除标记，0未删除，1删除
                .eq(BillItem::getIsDelete, 0)
        );
        for(BillItem item : billItemList){
            //已出库,锁定库存改为0
            item.setFreezeStock(0l);
            billItemService.updateById(item);
        }
        head.setBillItemList(billItemList);
        //生成销售出货单
        this.addOrder(head);
        //出库操作
        this.outStock(head);
        this.updateById(head);
        //出入库时间
        oldHead.setOperTime(new Date());
        return this.updateById(oldHead);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean addOrder(BillHead head) {
        if(StringUtils.isNotBlank(head.getCodeNum())){
            if(checkIsNameExist(null, head.getCodeNum()) > 0){
                throw new BusinessException("单据编号不能重复");
            }
        }else {
            //当天单据数量
            int num =  this.count(new LambdaQueryWrapper<BillHead>().eq(BillHead::getBillType, head.getBillType())
                    .ge(BillHead::getCreateTime, DateUtil.getDateString(new Date()) + " 00:00:00"));
            num = num + 1;
            head.setCodeNum(CodeUtils.geneBillCode(BillTypeEnum.getCode(head.getBillType()),num));
        }
        head.setTypeName(BillTypeEnum.getName(head.getBillType()));
        if(head.getCreatorId() == null || head.getCreatorId() <= 0){
            head.setCreatorId(ShopLoginUtil.getUserId());
        }
        //附件List
        if(CollectionUtils.isNotEmpty(head.getFileList())){
            head.setFileUrls(String.join(",", head.getFileList()));
        }
        //检查单据属性
        this.checkHead(head);
        List<BillItem> billItemList = head.getBillItemList();
        //期初库存单
        if(BillTypeEnum.QCKCD.getValue().equals(head.getBillType())){
            //只拿修改了的数据
            billItemList = billItemList.stream().filter(item -> item.getBasicNumber() != null && item.getBasicNumber() > 0).collect(Collectors.toList());
        }
        //盘盈盘亏单
        if(BillTypeEnum.PYPKD.getValue().equals(head.getBillType())){
            //只拿修改了的数据
            billItemList = billItemList.stream().filter(item -> item.getBasicNumber() != null && item.getBasicNumber() != 0).collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(billItemList)){
            throw new BusinessException("商品不能为空");
        }
        //售后类型(10退货退款 20换货 30退款 40补发 50 维修 60 其他)
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())){
            //售后类型(0无售后 10退货退款 20换货 30仅退款 40补发 50 维修 60 其他)"
            if(head.getRefundType() == 20){
                //是否换出商品,0否,1是
                List<BillItem> outItemList = billItemList.stream().filter(item -> item.getIsExchange() != null && item.getIsExchange() == 1).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(outItemList)){
                    throw new BusinessException("换出商品不能为空");
                }
            }
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        Long totalNum = 0L;
        for(BillItem item : billItemList){
            item.setBusinessType(head.getBusinessType());
            item.setBillType(head.getBillType());
            item.setSupplierId(head.getSupplierId());
            item.setDepotId(head.getDepotId());
            item.setBillItemId(null);
            item.setIsExchange(head.getIsExchange());
            //检查商品属性
            this.checkItem(item);
            if(item.getAllPrice() != null){
                totalPrice = totalPrice.add(item.getAllPrice());
            }
            totalNum = totalNum + item.getBasicNumber();
        }
        head.setTotalPrice(totalPrice);
        head.setTotalNum(totalNum);
        head.setBillId(null);
        //线下出库单
        if(BillTypeEnum.XSCKD.getValue().equals(head.getBillType()) && head.getBusinessType() != null && BusinessTypeEnum.XXCKD.getValue().equals(head.getBusinessType())){
            head.setTypeName("线下出库单");
        }
        this.save(head);
        //销售订单,售后申请单,线下出库单,其他出库单
        if(BillTypeEnum.XSDD.getValue().equals(head.getBillType()) || BillTypeEnum.SHSQD.getValue().equals(head.getBillType())
                || (head.getBusinessType() != null && BusinessTypeEnum.XXCKD.getValue().equals(head.getBusinessType()))
                || BillTypeEnum.QTCKD.getValue().equals(head.getBillType())
        ) {
            BillAddress billAddress = head.getBillAddress();
            if(billAddress == null){
                throw new BusinessException("收货地址不能为空");
            }
            billAddress.setBillId(head.getBillId());
            //手机
            if(StringUtils.isBlank(billAddress.getPhone()) && StringUtils.isBlank(billAddress.getFixPhone())){
                throw new BusinessException("手机或固话至少填一个");
            }
            //详细地址
            if(StringUtils.isBlank(billAddress.getDetail())){
                throw new BusinessException("详细地址不能为空");
            }
            //省市区不能为空
            if(billAddress.getProvinceId() == null || billAddress.getCityId() ==null || billAddress.getRegionId() == null){
                throw new BusinessException("省市区不能为空");
            }
            billAddress.setCreateTime(new Date());
            billAddressService.save(billAddress);
        }
        //保存单据商品
        for(BillItem item : billItemList){
            item.setBillId(head.getBillId());
            billItemService.save(item);
        }
        //不用审核的出入库单 (线下出库单 期初库存单 调拨出库单 调拨入库单 其他出库单 其他入库单 盘盈盘亏单)
        if( (BillTypeEnum.XSCKD.getValue().equals(head.getBillType()) && head.getBusinessType() != null && BusinessTypeEnum.XXCKD.getValue().equals(head.getBusinessType()))
                || BillTypeEnum.QCKCD.getValue().equals(head.getBillType()) || BillTypeEnum.XSTHD.getValue().equals(head.getBillType())
                || BillTypeEnum.DBCKD.getValue().equals(head.getBillType()) || BillTypeEnum.DBRKD.getValue().equals(head.getBillType())
                || BillTypeEnum.QTCKD.getValue().equals(head.getBillType()) || BillTypeEnum.QTRKD.getValue().equals(head.getBillType())
                || BillTypeEnum.PYPKD.getValue().equals(head.getBillType())
        ){
            //出入库操作
            this.outStock(head);
            head.setAuditStatus(20);
            this.updateById(head);
        }
        //库存虚拟调拨单
        if((BillTypeEnum.KCDBD.getValue().equals(head.getBillType()) && BusinessTypeEnum.XNDB.getValue().equals(head.getBusinessType()))){
            for(BillItem item : billItemList){
                //关联的订单子单号
                item.setLinkItemId(item.getBillItemId());
            }
            head.setLinkBillId(head.getBillId().toString());
            head.setLinkBillNum(head.getCodeNum());
            //自动生成调拨出库单
            head.setBillType(BillTypeEnum.DBCKD.getValue());
            head.setCodeNum(null);
            head.setBillId(null);
            this.addOrder(head);
            //自动生成调拨入库单
            head.setBillType(BillTypeEnum.DBRKD.getValue());
            head.setCodeNum(null);
            head.setBillId(null);
            //入库交换仓库ID
            Long depotId = head.getDepotId();
            head.setDepotId(head.getAnotherDepotId());
            head.setAnotherDepotId(depotId);
            this.addOrder(head);
        }
        //销售退货单 原路退回不验证快递 业务类型,7原路退回
        if((BillTypeEnum.XSTHD.getValue().equals(head.getBillType())) && (head.getBusinessType() == null || !BusinessTypeEnum.XTYLTH.getValue().equals(head.getBusinessType()))){
            BillDelivery oldDelivery = new BillDelivery();
            if(head.getExpressId() == null){
                throw new BusinessException("物流公司不能为空");
            }
            if(StringUtils.isBlank(head.getExpressNo())){
                throw new BusinessException("快递单号不能为空");
            }
            if(checkIsExpressNoExist(null, head.getExpressNo()) > 0){
                throw new BusinessException("快递单号已存在");
            }
            //物流公司id
            oldDelivery.setExpressId(head.getExpressId());
            oldDelivery.setBillId(head.getBillId());
            Express express = expressService.getById(head.getExpressId());
            if(express != null){
                oldDelivery.setExpressName(express.getExpressName());
            }
            oldDelivery.setExpressNo(head.getExpressNo());
            billDeliveryService.save(oldDelivery);
        }
        return true;
    }

    //檢查
    public int checkIsExpressNoExist(Long id, String expressNo) {
        LambdaQueryWrapper<BillDelivery> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(BillDelivery::getBillDeliveryId, id);
        }
        wrapper.eq(BillDelivery::getExpressNo, expressNo);
        return billDeliveryService.count(wrapper);
    }

    //检查单据属性
    private void checkHead(BillHead head) {
        //售后类型(0无售后 10退货退款 20换货 30仅退款 40补发 50 维修 60 其他)
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())){
            if(head.getRefundType() == null){
                throw new RuntimeException("售后类型不能为空");
            }
            if(head.getRefundType() == 30){
                head.setDepotId(0L);
            }
        }
        //销售订单新增时非必填,审核时必填
        if(head.getDepotId() == null && !BillTypeEnum.XSDD.getValue().equals(head.getBillType())){
            throw new BusinessException("仓库不能为空");
        }
        if(head.getBusinessTime() == null){
            head.setBusinessTime(new Date());
        }
        //售后申请单
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())) {
            if (head.getRefundType() == null) {
                throw new BusinessException("售后类型不能为空");
            }
            if( StringUtils.isBlank(head.getLinkBillId()) || StringUtils.isBlank(head.getLinkBillNum())){
                throw new BusinessException("关联销售单号不能为空");
            }
            if( StringUtils.isBlank(head.getProblemCause())){
                throw new BusinessException("问题原因不能为空");
            }
            if(head.getCustomerId() == null ){
                throw new BusinessException("客户不能为空");
            }
            if(head.getStoreId() == null ){
                throw new BusinessException("店铺不能为空");
            }
            if(head.getAccountId() == null ){
                throw new BusinessException("结算账户不能为空");
            }
            if("物流赔付".equals(head.getProblemCause()) && (head.getCompensateMoney() == null || head.getCompensateMoney().compareTo(BigDecimal.ZERO) <= 0)){
                throw new BusinessException("赔付金额不能为空且必须大于0");
            }
        }
        //库存调拨单
        if(BillTypeEnum.KCDBD.getValue().equals(head.getBillType()) || BillTypeEnum.DBCKD.getValue().equals(head.getBillType())
                || BillTypeEnum.DBRKD.getValue().equals(head.getBillType())
        ) {
            //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨
            if (head.getBusinessType() == null) {
                head.setBusinessType(BusinessTypeEnum.SJDB.getValue());
            }
            if (head.getAnotherDepotId() == null) {
                throw new BusinessException("对方仓库不能为空");
            }
            if(Objects.equals(head.getDepotId(), head.getAnotherDepotId())){
                throw new BusinessException("调拨仓库不能相同");
            }
            if(BillTypeEnum.DBCKD.getValue().equals(head.getBillType())
                    || BillTypeEnum.DBRKD.getValue().equals(head.getBillType())
            ){
                if( StringUtils.isBlank(head.getLinkBillId()) || StringUtils.isBlank(head.getLinkBillNum())){
                    throw new BusinessException("关联单号不能为空");
                }
            }
        }
        //采购入库单
        if(BillTypeEnum.CGRKD.getValue().equals(head.getBillType()) || BillTypeEnum.CGTHD.getValue().equals(head.getBillType())){
            if(head.getBusinessType() == null){
                throw new BusinessException("业务类型不能为空");
            }
            if(BusinessTypeEnum.DDCGRK.getValue().equals(head.getBusinessType()) || BusinessTypeEnum.CGRKDTH.getValue().equals(head.getBusinessType())){
                if( StringUtils.isBlank(head.getLinkBillId()) || StringUtils.isBlank(head.getLinkBillNum())){
                    throw new BusinessException("关联单号不能为空");
                }
            }
            if(head.getDiscountMoney() == null && BillTypeEnum.CGRKD.getValue().equals(head.getBillType())){
                throw new BusinessException("优惠金额不能为空");
            }
            if(head.getDeposit() == null){
                throw new BusinessException("付款金额或者退回金额不能为空");
            }
            if(head.getTotalPrice() == null){
                throw new BusinessException("合计金额不能为空");
            }
            if(head.getPurchaseMoney() == null){
                head.setPurchaseMoney(BigDecimal.ZERO);
            }
            if(StringUtils.isBlank(head.getPayType())){
                head.setPayType("0");
            }
            if(head.getAccountId() == null){
                throw new BusinessException("结算账户不能为空");
            }
            if(head.getDeptMoney() == null){
                throw new BusinessException("欠款金额不能为空");
            }
            if(head.getDeptMoney().compareTo(BigDecimal.ZERO) < 0){
                throw new BusinessException("欠款金额不能小于0");
            }
        }
        //销售单
        if(BillTypeEnum.XSDD.getValue().equals(head.getBillType()) || BillTypeEnum.XSCKD.getValue().equals(head.getBillType())
                ){

            if(BillTypeEnum.XSDD.getValue().equals(head.getBillType()) || BillTypeEnum.XSCKD.getValue().equals(head.getBillType())){
                if(head.getOrderTime() == null ){
                    head.setOrderTime(new Date());
                }
                if(StringUtils.isBlank(head.getLinkBillNum())){
                    //自动生成订单号
                    head.setLinkBillNum(OrderUtils.geneOrderNo(ShopLoginUtil.getUserId()));
                }
            }
            if(head.getCustomerId() == null ){
                throw new BusinessException("客户不能为空");
            }
            if(head.getStoreId() == null ){
                throw new BusinessException("店铺不能为空");
            }
            if(head.getAccountId() == null){
                throw new BusinessException("结算账户不能为空");
            }
            //是否换货单,0否,1是
            if(head.getIsExchange() == null || head.getIsExchange() == 0){
                if(head.getPurchaseMoney() == null){
                    throw new BusinessException("运费不能为空");
                }
                if(head.getDiscountMoney() == null ){
                    throw new BusinessException("优惠金额不能为空");
                }
                if(head.getDeposit() == null){
                    throw new BusinessException("实收金额不能为空");
                }
                if(head.getTotalPrice() == null){
                    throw new BusinessException("合计金额不能为空");
                }
                if(head.getDeptMoney() == null){
                    throw new BusinessException("欠款金额不能为空");
                }
                if(head.getDeptMoney().compareTo(BigDecimal.ZERO) < 0){
                    throw new BusinessException("欠款金额不能小于0");
                }
            }

        }
        //其他出库单
        if(BillTypeEnum.QTCKD.getValue().equals(head.getBillType()) || BillTypeEnum.QTRKD.getValue().equals(head.getBillType())) {
            if (head.getBusinessCause() == null) {
                throw new BusinessException("业务原因不能为空");
            }
            if(head.getHandleId() == null || head.getHandleId() <= 0){
                throw new BusinessException("经手人不能为空");
            }
            if (BillTypeEnum.QTCKD.getValue().equals(head.getBillType())) {
                if(head.getCustomerId() == null ){
                    throw new BusinessException("收货人不能为空");
                }
            }
        }
    }

    //检查商品属性
    private void checkItem(BillItem item) {
        if(item.getProductId() == null){
            throw new BusinessException("商品Id不能为空");
        }
        if(item.getProductExtendId() == null){
            throw new BusinessException("商品规格id不能为空");
        }
        if(item.getBasicNumber() == null){
            throw new BusinessException("操作总数量不能为空");
        }
        if(item.getBasicNumber() == 0){
            throw new BusinessException("操作总数量不能为0");
        }
        ProductExtend extend = productExtendService.getById(item.getProductExtendId());
        if(extend == null){
            throw new BusinessException("商品规格id"+item.getProductExtendId()+"不存在");
        }
        //可操作总数量
        item.setAvailableAllNumber(item.getBasicNumber());
        //16期初库存单 15盘盈盘亏单 纯库存操作不验证
        if(!BillTypeEnum.QCKCD.getValue().equals(item.getBillType()) && !BillTypeEnum.PYPKD.getValue().equals(item.getBillType())){
//            if(StringUtils.isBlank(item.getSku())){
//                throw new BusinessException("属性不能为空");
//            }
            if(StringUtils.isBlank(item.getMaterialUnit())){
                throw new BusinessException("商品单位不能为空");
            }
            if(item.getOperNumber() == null){
                throw new BusinessException("商品数量不能为空");
            }
            if(item.getOperNumber() <= 0){
                throw new BusinessException("商品数量需大于0");
            }
            //单价
            if(item.getUnitPrice() == null){
                item.setUnitPrice(BigDecimal.ZERO);
            }
            //折扣率
            if(item.getDiscountRate() == null){
                item.setDiscountRate(new BigDecimal("100"));
            }
            //折后单价
            if(item.getDiscountPrice() == null){
                item.setDiscountPrice(BigDecimal.ZERO);
            }
            //总价
            if(item.getAllPrice() == null){
                item.setAllPrice(BigDecimal.ZERO);
            }
        }else if(BillTypeEnum.QCKCD.getValue().equals(item.getBillType())){
            //是否可以修改期初成本价,0否,1是
            if(extend != null && extend.getInitialStatus() == 0){
                throw new BusinessException("规格编码:" + extend.getProductCode() + "的期初库存不能修改");
            }
        }
        //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨
        if(item.getBusinessType() != null && BusinessTypeEnum.DDCGRK.getValue().equals(item.getBusinessType())){
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的订单子单号不能为空");
            }
            //关联采购订单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            if(item.getBasicNumber() > billItem.getAvailableAllNumber()){
                throw new BusinessException("不能超收,规格编码:" + extend.getProductCode() + ",可入库总数:" + billItem.getAvailableAllNumber() + ",实收总数:" + item.getBasicNumber());
            }
            //未到货数量 = 可入库数 - 实收数
            item.setNoArrivalNumber(billItem.getAvailableNumber() - item.getOperNumber());
            //未到货总数量 = 可入库总数 - 实收总数
            item.setNoArrivalAllNumber(billItem.getAvailableAllNumber() - item.getBasicNumber());
        }
        //11调拨出库单
        if(BillTypeEnum.DBCKD.getValue().equals(item.getBillType())){
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的订单子单号不能为空");
            }
            //关联调拨单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //可操作总数量
            Long availableAllNumber = billItem.getAvailableAllNumber();
            if(item.getBasicNumber() > availableAllNumber){
                throw new BusinessException("不能超出,规格编码:" + extend.getProductCode() + ",可操作总数:" + availableAllNumber + ",操作总数:" + item.getBasicNumber());
            }
        }
        //调拨入库单
        if(BillTypeEnum.DBRKD.getValue().equals(item.getBillType())){
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的订单子单号不能为空");
            }
            //关联采购订单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //待入库总数量
            Long noArrivalAllNumber = billItem.getNoArrivalAllNumber();
            if(item.getBasicNumber() > noArrivalAllNumber){
                throw new BusinessException("不能超入,规格编码:" + extend.getProductCode() + ",可操作总数:" + noArrivalAllNumber + ",操作总数:" + item.getBasicNumber());
            }
        }
        //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨
        if(item.getBusinessType() != null && BusinessTypeEnum.CGRKDTH.getValue().equals(item.getBusinessType())){
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的采购入库单子单号不能为空");
            }
            //关联的采购入库单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //可退数量
            if(item.getBasicNumber() > billItem.getAvailableAllNumber() ){
                throw new BusinessException("实退数量不能大于可退数量,商品编码:" + extend.getProductCode() + ",可退数量:" + billItem.getAvailableAllNumber() + ",实退数量:" + item.getBasicNumber());
            }
        }
        if(BillTypeEnum.SHSQD.getValue().equals(item.getBillType())) {
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的销售订单子单号不能为空");
            }
            //关联的销售订单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //可退数量
            if(item.getBasicNumber() > billItem.getAvailableAllNumber()){
                if(extend == null){
                    throw new BusinessException("商品规格ID"+item.getProductExtendId()+"不存在");
                }
                throw new BusinessException("实退数量不能大于可退数量,商品编码:" + extend.getProductCode() + ",可退数量:" + billItem.getAvailableAllNumber() + ",实退数量:" + item.getBasicNumber());
            }
        }
        if(BillTypeEnum.XSTHD.getValue().equals(item.getBillType())) {
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的售后单子单号不能为空");
            }
            //关联的售后单子单号
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //可退数量
            if(item.getBasicNumber() > billItem.getAvailableAllNumber()){
                throw new BusinessException("入库数量不能大于可入库数量,商品编码:" + extend.getProductCode() + ",可入库数量:" + billItem.getAvailableAllNumber() + ",入库数量:" + item.getBasicNumber());
            }
        }
        //换货订单
        if(BillTypeEnum.XSDD.getValue().equals(item.getBillType()) && item.getIsExchange() != null && item.getIsExchange() == 1) {
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的售后单子单号不能为空");
            }
            //关联的售后单子单号
            BillItem billItem = billItemService.getById(item.getLinkItemId());
            //可下单数量
            if(item.getBasicNumber() > billItem.getAvailableAllNumber()){
                throw new BusinessException("下单数量不能大于可下单数量,商品编码:" + extend.getProductCode() + ",可下单数量:" + billItem.getAvailableAllNumber() + ",下单数量:" + item.getBasicNumber());
            }
            //减少可下单数量
            billItem.setAvailableAllNumber(billItem.getAvailableAllNumber() - item.getBasicNumber());
            billItemService.updateById(billItem);
        }

        //单据类型
        if(item.getBillType() != null && BillTypeEnum.CGDD.getValue().equals(item.getBillType())){
            if(item.getPurchaseCycle() == null){
                throw new BusinessException("采购周期不能为空");
            }
//            if(item.getArrivalDate() == null){
//                throw new BusinessException("预计到货日期不能为空");
//            }
        }

        //单据类型,销售单,库存调拨单
        if(item.getBillType() != null && (BillTypeEnum.XSDD.getValue().equals(item.getBillType()) || BillTypeEnum.KCDBD.getValue().equals(item.getBillType())) ){
            //锁定库存,实物数量中已经被销售订单、库存调拨单锁定的数量
            item.setFreezeStock(item.getBasicNumber());
        }
    }

    public int checkIsNameExist(Long id, String code) {
        LambdaQueryWrapper<BillHead> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(BillHead::getBillId, id);
        }
        wrapper.eq(BillHead::getCodeNum, code);
        return this.count(wrapper);
    }

    @Override
    public boolean editOrder(BillHead head) {
        if(head.getBillId() == null){
            throw new BusinessException("单据ID不能为空");
        }
        BillHead oldHead = this.getById(head.getBillId());
        //审核状态，0未审核、1审核通过,2审核拒绝
        if(oldHead.getAuditStatus() != 10){
            throw new BusinessException("只有待审核订单才能修改");
        }
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(oldHead.getPurchaseStatus() == 1){
            throw new BusinessException("已完成状态不能修改");
        }
        if(StringUtils.isBlank(head.getCodeNum())){
            throw new BusinessException("编辑时单据编号不能为空");
        }else {
            if(checkIsNameExist(head.getBillId(), head.getCodeNum()) > 0){
                throw new BusinessException("单据编号不能重复");
            }
        }
        //附件List
        if(CollectionUtils.isNotEmpty(head.getFileList())){
            head.setFileUrls(String.join(",", head.getFileList()));
        }
        //检查单据属性
        this.checkHead(head);
        List<BillItem> billItemList = head.getBillItemList();
        if(CollectionUtils.isEmpty(billItemList)){
            throw new BusinessException("商品不能为空");
        }
        //售后类型(10退货退款 20换货 30退款 40补发 50 维修 60 其他)
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())){
            //售后类型(0无售后 10退货退款 20换货 30仅退款 40补发 50 维修 60 其他)"
            if(head.getRefundType() == 20){
                //是否换出商品,0否,1是
                List<BillItem> outItemList = billItemList.stream().filter(item -> item.getIsExchange() != null && item.getIsExchange() == 1).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(outItemList)){
                    throw new BusinessException("换出商品不能为空");
                }
            }
            if(head.getAddress() != null){
                this.editAddress(head.getAddress());
            }
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        Long totalNum = 0L;
        for(BillItem item : billItemList){
            item.setBusinessType(head.getBusinessType());
            item.setBillType(oldHead.getBillType());
            //检查商品属性
            this.checkItem(item);
            if(item.getAllPrice() != null){
                if(item.getIsDelete() == null || item.getIsDelete() == 0){
                    totalPrice = totalPrice.add(item.getAllPrice());
                }
            }
            totalNum = totalNum + item.getBasicNumber();
        }
        head.setTotalPrice(totalPrice);
        head.setTotalNum(totalNum);
        this.updateById(head);
        //保存单据商品
        for(BillItem item : billItemList){
            item.setBillId(head.getBillId());
            if(item.getBillItemId() != null){
                billItemService.updateById(item);
            }else {
                item.setSupplierId(head.getSupplierId());
                billItemService.save(item);
            }
        }
        return true;
    }

    @Override
    public boolean closeOrder(Long billId, String closeReason) {
        BillHead oldHead = this.getById(billId);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(oldHead.getPurchaseStatus() != 0){
            throw new BusinessException("该状态不能关闭");
        }
        oldHead.setPurchaseStatus(3);
        //审核状态，10待审核20通过30拒绝
        oldHead.setAuditStatus(30);
        oldHead.setCloseReason(closeReason);
        return this.updateById(oldHead);
    }

    //关闭异常订单
    @Override
    public boolean closeMarketOrder(Long billId, String closeReason) {
        BillHead oldHead = this.getById(billId);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(oldHead.getPurchaseStatus() != 4){
            throw new BusinessException("非异常订单不能关闭");
        }
        oldHead.setPurchaseStatus(3);
        oldHead.setCloseReason(closeReason);
        return this.updateById(oldHead);
    }

    //恢复异常订单
    @Override
    public boolean recoverMarketOrder(Long billId) {
        BillHead oldHead = this.getById(billId);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        if(oldHead.getPurchaseStatus() != 4){
            throw new BusinessException("只能恢复异常订单");
        }
        oldHead.setPurchaseStatus(0);
        //审核状态，10待审核20通过30拒绝
        oldHead.setAuditStatus(10);
        return this.updateById(oldHead);
    }

    //确认配货
    @Override
    public boolean picking(Long billId) {
        BillHead oldHead = this.getById(billId);
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(2);
        return this.updateById(oldHead);
    }

    //审核销售订单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditMarketOrder(BillHead head) {
        AdminUser adminUser = shopUserService.getById(ShopLoginUtil.getUserId());
        BillHead oldHead = this.getById(head.getBillId());
        if(oldHead == null){
            throw new BusinessException("单据不能为空");
        }
        //审核状态，0未审核、1审核通过,2审核拒绝
        if(oldHead.getAuditStatus() != 10){
            throw new BusinessException("只有待审核订单才能审核");
        }
        if(oldHead.getDepotId() == null){
            throw new BusinessException("审核销售订单时仓库不能为空");
        }

        oldHead.setAuditId(adminUser.getShopUserId());
        //审核状态，10待审核20通过30拒绝
        oldHead.setAuditStatus(20);
        //单据状态，0未采购、1完成采购|销售、2进行中,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(5);
        return this.updateById(oldHead);
    }

    //审核售后单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRefundOrder(BillHead head) {
        BillHead oldHead = this.getById(head.getBillId());
        AdminUser adminUser = shopUserService.getById(ShopLoginUtil.getUserId());
        if(StringUtils.isBlank(oldHead.getLinkBillNum())){
            throw new BusinessException("关联单据编号不能为空");
        }
        List<String> linkBillNumList = Arrays.asList(oldHead.getLinkBillNum().split(","));
        //修改关联销售单的售后类型
        this.update(new LambdaUpdateWrapper<BillHead>().in(BillHead::getCodeNum, linkBillNumList)
                //售后类型(10退货退款 20换货 30退款 40补发 50 维修 60 其他)
                .set(BillHead::getRefundType, oldHead.getRefundType())
        );
        //审核状态，0未审核、1审核通过,2审核拒绝
        if(oldHead.getAuditStatus() != 10){
            throw new BusinessException("只有待审核订单才能审核");
        }
        oldHead.setAuditId(adminUser.getShopUserId());
        //审核状态，10待审核20通过30拒绝
        oldHead.setAuditStatus(20);
        //单据状态，0未开始、1完成、2进行中,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(2);
        //售后类型(0无售后 10退货退款 20换货 30仅退款 40补发 50 维修 60 其他)
        if(oldHead.getRefundType() == 30){
            //客户赔付结算
            this.customerCompensate(oldHead);
            //仅退款无需退货,退款后改成已完成
            oldHead.setPurchaseStatus(1);
        }
        List<BillItem> billItemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                .eq(BillItem::getBillId, oldHead.getBillId())
                //删除标记，0未删除，1删除
                .eq(BillItem::getIsDelete, 0)
        );
        for(BillItem item : billItemList){
            if( item.getLinkItemId() == null ){
                throw new BusinessException("关联的销售订单子单号不能为空");
            }
            //关联的销售订单子单
            BillItem billItem = billItemService.getById(item.getLinkItemId());

            //可退数量
            if(item.getBasicNumber() > billItem.getAvailableAllNumber()){
                ProductExtend extend = productExtendService.getById(item.getProductExtendId());
                if(extend == null){
                    throw new BusinessException("商品规格ID"+item.getProductExtendId()+"不存在");
                }
                throw new BusinessException("实退数量不能大于可退数量,商品编码:" + extend.getProductCode() + ",可退数量:" + billItem.getAvailableAllNumber() + ",实退数量:" + item.getBasicNumber());
            }

            //减少可退数量
            billItem.setAvailableAllNumber(billItem.getAvailableAllNumber() - item.getBasicNumber());
            billItemService.updateById(billItem);
        }
        return this.updateById(oldHead);
    }

    //打回审核
    @Override
    public boolean repulseAudit(Long billId) {
        BillHead head = this.getById(billId);
        //售后申请单
        if(BillTypeEnum.SHSQD.getValue().equals(head.getBillType())){
            LambdaQueryWrapper<BillHead> wrapperRk = new LambdaQueryWrapper<>();
            wrapperRk.orderByDesc(BillHead::getCreateTime);
            wrapperRk.eq(BillHead::getBillType, BillTypeEnum.XSTHD.getValue());
            wrapperRk.apply("FIND_IN_SET({0}, link_bill_num)",head.getCodeNum());
            //入库单
            List<BillHead> rKList = this.list(wrapperRk);
            if(CollectionUtils.isNotEmpty(rKList)){
                throw new BusinessException("已入库不能打回");
            }
            LambdaQueryWrapper<BillHead> wrapperXd = new LambdaQueryWrapper<>();
            wrapperXd.orderByDesc(BillHead::getCreateTime);
            wrapperXd.eq(BillHead::getBillType, BillTypeEnum.XSDD.getValue());
            wrapperXd.apply("FIND_IN_SET({0}, link_bill_num)",head.getCodeNum());
            //入库单
            List<BillHead> xDList = this.list(wrapperXd);
            if(CollectionUtils.isNotEmpty(xDList)){
                throw new BusinessException("已下单不能打回");
            }
        }
        //销售单
        if(BillTypeEnum.XSDD.getValue().equals(head.getBillType())){
            //单据状态，0未开始、1完成、2进行中,3已关闭,4异常,5待配货,6已发货
            if(head.getPurchaseStatus() != 5){
                throw new BusinessException("只有待配货订单才能打回");
            }
        }

        head.setAuditId(0);
        //审核状态，10待审核20通过30拒绝
        head.setAuditStatus(10);
        //单据状态，0未开始、1完成、2进行中,3已关闭,4异常,5待配货,6已发货
        head.setPurchaseStatus(0);
        return this.updateById(head);
    }

    //提交异常订单
    @Override
    public boolean abnormal(BillHead head) {
        BillHead oldHead = this.getById(head.getBillId());
        if(StringUtils.isBlank(head.getAnomalousReason())){
            throw new BusinessException("异常原因不能为空");
        }
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(4);
        return this.updateById(oldHead);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditOrder(BillHead head) {
        AdminUser adminUser = shopUserService.getById(ShopLoginUtil.getUserId());
        BillHead oldHead = this.getById(head.getBillId());
        //审核状态，0未审核、1审核通过,2审核拒绝
        if(oldHead.getAuditStatus() != 10){
            throw new BusinessException("只有待审核订单才能审核");
        }
        oldHead.setAuditId(adminUser.getShopUserId());
        oldHead.setAuditStatus(head.getAuditStatus());
        //审核状态，10待审核20通过30拒绝
        if(head.getAuditStatus() == 30){
            if(StringUtils.isEmpty(head.getRefuseReason())){
                throw new BusinessException("拒绝原因不能为空");
            }
            //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
            oldHead.setPurchaseStatus(3);
            oldHead.setRefuseReason(head.getRefuseReason());
        }
        //审核状态，10待审核20通过30拒绝
        if(head.getAuditStatus() == 20){
            //审核成功,出入库操作
            this.outStock(oldHead);
        }

        return this.updateById(oldHead);
    }

    //出入库操作
    private void outStock(BillHead oldHead) {
        //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
        oldHead.setPurchaseStatus(1);
        //出入库时间
        oldHead.setOperTime(new Date());
        //如果是出入库类型
        if(BillTypeEnum.getStockValues().contains(oldHead.getBillType())) {
            List<BillItem> billItemList = billItemService.list(new LambdaQueryWrapper<BillItem>()
                    .eq(BillItem::getBillId, oldHead.getBillId())
                    //删除标记，0未删除，1删除
                    .eq(BillItem::getIsDelete, 0)
            );
            for(BillItem item : billItemList){
                ProductExtend extend = productExtendService.getById(item.getProductExtendId());
                if(extend != null && item.getBasicNumber() != 0){
                    if(BillTypeEnum.getFoldValues().contains(oldHead.getBillType())){
                        //入库,累积库存
                        productUtils.addStock(extend, item);
                    }
                    if(BillTypeEnum.getOutValues().contains(oldHead.getBillType())){
                        //出库,减少库存
                        productUtils.subtractStock(extend, item);
                    }
                }
                //未到货数量
                if(item.getNoArrivalNumber() != null && item.getNoArrivalNumber() > 0){
                    //单据状态，0未采购、1完成采购|销售、2部分采购|销售单待发货,3已关闭,4异常,5待配货,6已发货
                    oldHead.setPurchaseStatus(2);
                }
            }
            BillHead newHead = this.getById(oldHead.getBillId());
            //运费合计
            if(newHead.getPurchaseMoney() != null && newHead.getPurchaseMoney().compareTo(BigDecimal.ZERO) > 0
             && newHead.getAccountId() > 0){
                //运费结算
                this.purchaseSettlement(newHead);
            }
            if(BillTypeEnum.XSCKD.getValue().equals(oldHead.getBillType())
            ){
                //客户结算
                this.customerSettlement(newHead);
            }else if(BillTypeEnum.CGRKD.getValue().equals(oldHead.getBillType())
                    || BillTypeEnum.CGTHD.getValue().equals(oldHead.getBillType())){
                //供应商结算
                this.supplierSettlement(newHead);
            }else if ( BillTypeEnum.XSTHD.getValue().equals(oldHead.getBillType())){
                //客户赔付结算
                this.customerCompensate(newHead);
            }
            //欠款金额
            if(oldHead.getDeptMoney() != null && oldHead.getDeptMoney().compareTo(BigDecimal.ZERO) > 0){
                //结算状态，0待结算、1已结算、2已结清
                oldHead.setSettlementStatus(1);
            }else {
                oldHead.setSettlementStatus(2);
            }
        }
    }

    //客户赔付结算
    private void customerCompensate(BillHead head) {
        // 记录日志
        AccountMoneyLog log = new AccountMoneyLog();
        //收支类型,1支出,2收入
        log.setDirectionType(1);
        //交易往来单位,0其他,1客户,2供应商,3快递
        log.setTradingUnit(1);
        //供应商ID
        log.setSupplierId(head.getSupplierId());
        //客户id
        log.setCustomerId(head.getCustomerId());
        List<BillDelivery> deliveryList = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, head.getBillId())
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        if(CollectionUtils.isNotEmpty(deliveryList)){
            //物流公司id
            log.setExpressId(deliveryList.get(0).getExpressId());
        }
        //账户id
        log.setAccountId(head.getAccountId());
        //单据id
        log.setBillId(head.getBillId());
        //店铺id
        log.setStoreId(head.getStoreId());
        //单据类型
        log.setBillType(head.getBillType());
        //变动金额
        log.setMoney(head.getTotalPrice().negate());
        //摘要
        log.setDescription(String.format(head.getTypeName() + ",单号：%s", head.getCodeNum()));
        //业务时间
        log.setBillTime(head.getBusinessTime());

        // 更新账户余额
        Account account = accountService.getById(head.getAccountId());
        //当前余额 + 本次变动金额
        account.setNowMoney(account.getNowMoney().add(log.getMoney()));
        accountService.updateById(account);

        log.setBalanceMoney(account.getNowMoney());
        accountMoneyLogService.save(log);

        //客户结算
        Customer customer = customerService.getById(head.getCustomerId());
        //当前应付 + 本次变动金额
        customer.setAllNeedPay(customer.getAllNeedPay().add(log.getMoney()));
        //已付预付款 + 本次变动金额
        customer.setDeposit(customer.getDeposit().add(log.getMoney()));
        customerService.updateById(customer);
    }

    //客户结算
    private void customerSettlement(BillHead head) {
        // 记录日志
        AccountMoneyLog log = new AccountMoneyLog();
        //销售出库单
        if(BillTypeEnum.XSCKD.getValue().equals(head.getBillType())){
            //收支类型,1支出,2收入
            log.setDirectionType(2);
        }
        //交易往来单位,0其他,1客户,2供应商,3快递
        log.setTradingUnit(1);
        //供应商ID
        log.setSupplierId(head.getSupplierId());
        //客户id
        log.setCustomerId(head.getCustomerId());
        List<BillDelivery> deliveryList = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, head.getBillId())
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        if(CollectionUtils.isNotEmpty(deliveryList)){
            //物流公司id
            log.setExpressId(deliveryList.get(0).getExpressId());
        }

        //账户id
        log.setAccountId(head.getAccountId());
        //单据id
        log.setBillId(head.getBillId());
        //店铺id
        log.setStoreId(head.getStoreId());
        //单据类型
        log.setBillType(head.getBillType());
        //变动金额
        log.setMoney(head.getDeposit());
        //摘要
        log.setDescription(String.format(head.getTypeName() + ",单号：%s", head.getCodeNum()));
        //业务时间
        log.setBillTime(head.getBusinessTime());

        // 更新账户余额
        Account account = accountService.getById(head.getAccountId());
        //当前余额 + 本次付款金额
        account.setNowMoney(account.getNowMoney().add(head.getDeposit()));
        accountService.updateById(account);

        log.setBalanceMoney(account.getNowMoney());
        accountMoneyLogService.save(log);

        //客户结算
        Customer customer = customerService.getById(head.getCustomerId());
        //当前应付 + 合计金额
        customer.setAllNeedPay(customer.getAllNeedPay().add(head.getTotalPrice()));
        //已付预付款 + 本次预付金额
        customer.setDeposit(customer.getDeposit().add(head.getDeposit()));
        //欠款
        customer.setDebtCredit(customer.getDebtCredit().add(head.getDeptMoney()));
        customerService.updateById(customer);
    }

    //供应商结算
    private void supplierSettlement(BillHead head) {
        // 记录日志
        AccountMoneyLog log = new AccountMoneyLog();
        //采购入库
        if(BillTypeEnum.CGRKD.getValue().equals(head.getBillType())){
            //收支类型,1支出,2收入
            log.setDirectionType(1);
            //支出 变动金额取负值
            head.setDeposit(head.getDeposit().negate());
            head.setTotalPrice(head.getTotalPrice().negate());
        }
        //采购退货
        if(BillTypeEnum.CGTHD.getValue().equals(head.getBillType())){
            //收支类型,1支出,2收入
            log.setDirectionType(2);
            //收入 欠款金额取负值
            head.setDeptMoney(head.getDeptMoney().negate());
        }
        //交易往来单位,0其他,1客户,2供应商,3快递
        log.setTradingUnit(2);
        //供应商ID
        log.setSupplierId(head.getSupplierId());
        //客户id
        log.setCustomerId(head.getCustomerId());
        List<BillDelivery> deliveryList = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, head.getBillId())
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        if(CollectionUtils.isNotEmpty(deliveryList)){
            //物流公司id
            log.setExpressId(deliveryList.get(0).getExpressId());
        }

        //账户id
        log.setAccountId(head.getAccountId());
        //单据id
        log.setBillId(head.getBillId());
        //店铺id
        log.setStoreId(head.getStoreId());
        //单据类型
        log.setBillType(head.getBillType());
        //变动金额
        log.setMoney(head.getDeposit());
        //摘要
        log.setDescription(String.format(head.getTypeName() + ",单号：%s", head.getCodeNum()));
        //业务时间
        log.setBillTime(head.getBusinessTime());

        // 更新账户余额
        Account account = accountService.getById(head.getAccountId());
        if(account == null){
            throw new BusinessException("结算账户不能为空");
        }
        //当前余额 - 本次付款金额(退货单时加退回金额)
        account.setNowMoney(account.getNowMoney().add(head.getDeposit()));
        accountService.updateById(account);

        log.setBalanceMoney(account.getNowMoney());
        accountMoneyLogService.save(log);

        //商户结算
        Supplier supplier = supplierService.getById(head.getSupplierId());
        //当前应付 + 合计金额
        supplier.setAllNeedPay(supplier.getAllNeedPay().add(head.getTotalPrice()));
        //已付预付款 + 本次预付金额
        supplier.setAdvancePay(supplier.getAdvancePay().add(head.getDeposit()));
        //欠款
        supplier.setDebtCredit(supplier.getDebtCredit().add(head.getDeptMoney()));
        supplierService.updateById(supplier);

    }

    //采购运费结算
    private void purchaseSettlement(BillHead head) {
        // 更新账户余额
        Account account = accountService.getById(head.getAccountId());
        //当前余额 - 采购运费
        account.setNowMoney(account.getNowMoney().subtract(head.getPurchaseMoney()));
        accountService.updateById(account);

        // 记录日志
        AccountMoneyLog log = new AccountMoneyLog();
        //账户id
        log.setAccountId(head.getAccountId());
        //单据id
        log.setBillId(head.getBillId());
        //店铺id
        log.setStoreId(head.getStoreId());
        //单据类型
        log.setBillType(head.getBillType());
        //变动金额
        log.setMoney(head.getPurchaseMoney().negate());
        //摘要
        log.setDescription(String.format(head.getTypeName() + "付运费,单号：%s", head.getCodeNum()));
        //业务时间
        log.setBillTime(head.getBusinessTime());
        //收支方向,1支出,2收入
        log.setDirectionType(1);
        //交易往来单位,0其他,1客户,2供应商,3快递
        log.setTradingUnit(3);
        //供应商ID
        log.setSupplierId(head.getSupplierId());
        //客户id
        log.setCustomerId(head.getCustomerId());
        List<BillDelivery> deliveryList = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, head.getBillId())
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        if(CollectionUtils.isNotEmpty(deliveryList)){
            //物流公司id
            log.setExpressId(deliveryList.get(0).getExpressId());
        }
        log.setBalanceMoney(account.getNowMoney());
        accountMoneyLogService.save(log);
    }

    //修改快递
    @Override
    public boolean editDeliver(BillDelivery delivery) {
        BillDelivery oldDelivery = null;
        if(delivery.getBillId() == null){
            throw new BusinessException("单据ID不能为空");
        }
        List<BillDelivery> list = billDeliveryService.list(new LambdaQueryWrapper<BillDelivery>()
                .eq(BillDelivery::getBillId, delivery.getBillId())
                .eq(BillDelivery::getIsDelete, 0)
                .orderByDesc(BillDelivery::getCreateTime)
        );
        if(CollectionUtils.isEmpty(list)){
            oldDelivery = new BillDelivery();
            oldDelivery.setBillId(delivery.getBillId());
        }else {
            oldDelivery = list.get(0);
        }
        if(delivery.getExpressId() == null && StringUtils.isBlank(delivery.getExpressNo())){
            throw new BusinessException("物流公司和快递单号不能同时为空");
        }
        //物流公司id
        if(delivery.getExpressId() != null){
            oldDelivery.setExpressId(delivery.getExpressId());
            Express express = expressService.getById(delivery.getExpressId());
            if(express != null){
                oldDelivery.setExpressName(express.getExpressName());
            }
        }
        if(StringUtils.isNotBlank(delivery.getExpressNo())){
            oldDelivery.setExpressNo(delivery.getExpressNo());
        }
        if(oldDelivery.getBillDeliveryId() != null){
            billDeliveryService.updateById(oldDelivery);
        }else {
            billDeliveryService.save(oldDelivery);
        }
        return true;
    }

    //设置物流
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrderDelivery(BillHead head) {
        if(head.getBillId() == null){
            throw new BusinessException("单据id不能为空");
        }
        if(CollectionUtils.isEmpty(head.getDeliveryList())){
            throw new BusinessException("物流不能为空");
        }
        List<BillDelivery> deliveryList = head.getDeliveryList();
        //保存单据商品
        for(BillDelivery item : deliveryList){
            if(item.getExpressId() == null){
                throw new BusinessException("物流公司id不能为空");
            }
            if(item.getExpressName() == null){
                throw new BusinessException("物流公司名称不能为空");
            }
            if(item.getExpressNo() == null){
                throw new BusinessException("快递单号不能为空");
            }
            item.setBillId(head.getBillId());
            if(item.getBillDeliveryId() != null){
                if(checkIsExpressNoExist(item.getBillDeliveryId(), head.getExpressNo()) > 0){
                    throw new BusinessException("快递单号已存在");
                }
                billDeliveryService.updateById(item);
            }else {
                if(checkIsExpressNoExist(null, head.getExpressNo()) > 0){
                    throw new BusinessException("快递单号已存在");
                }
                billDeliveryService.save(item);
            }
        }
        return true;
    }
}
