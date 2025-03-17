package net.jjjerp.admin.service.supplier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.mapper.supplier.SupplierMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.supplier.SupplierService;
import net.jjjerp.common.util.CodeUtils;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商/客户信息表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-18
 */
@Slf4j
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private RegionCache regionCache;
    @Autowired
    private BillHeadService billHeadService;

    @Override
    public Paging<Supplier> getList(CommonPageParam param) {
        // 商品列表
        Page<Supplier> page = new PageInfo<>(param);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Supplier::getDeleteFlag, 0);
        //名称搜索
        if(StringUtils.isNotEmpty(param.getName())){
            wrapper.like(Supplier::getSupplierName, param.getName());
        }
        // 启用 0-禁用  1-启用
        if(param.getEnabled() != null && param.getEnabled() > -1){
            wrapper.eq(Supplier::getEnabled, param.getEnabled());
        }
        //编码
        if(StringUtils.isNotEmpty(param.getCode())){
            wrapper.like(Supplier::getSupplierCode, param.getCode());
        }
        wrapper.orderByAsc(Supplier::getSort);
        wrapper.orderByDesc(Supplier::getCreateTime);
        IPage<Supplier> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        return new Paging<>(iPage);
    }

    @Override
    public boolean add(Supplier supplier) {
        if(StringUtils.isEmpty(supplier.getSupplierCode())){
            //自动生成编码,年月日加四位随机数字
            supplier.setSupplierCode(CodeUtils.geneCode());
        }else {
            if(this.checkIsCodeExist(null, supplier.getSupplierCode()) > 0){
                throw new BusinessException("供应商编码已存在");
            }
        }
        if(CollectionUtils.isNotEmpty(supplier.getImageList())){
            supplier.setImageUrl(String.join(",", supplier.getImageList()));
        }
        //当前应付 = 期初应付
        supplier.setAllNeedPay(supplier.getInitial());
        //已付预付款 = 期初应付 - 欠款
        supplier.setAdvancePay(supplier.getInitial().subtract(supplier.getDebtCredit()));
        return this.save(supplier);
    }

    @Override
    public boolean edit(Supplier supplier) {
        if(StringUtils.isEmpty(supplier.getSupplierCode())){
            throw new BusinessException("供应商编码不能为空");
        }else {
            if(this.checkIsCodeExist(supplier.getSupplierId(), supplier.getSupplierCode()) > 0){
                throw new BusinessException("供应商编码已存在");
            }
        }
        if(CollectionUtils.isNotEmpty(supplier.getImageList())){
            supplier.setImageUrl(String.join(",", supplier.getImageList()));
        }
        return this.updateById(supplier);
    }

    public int checkIsCodeExist(Long id, String productCode) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Supplier::getSupplierId, id);
        }
        wrapper.eq(Supplier::getSupplierCode, productCode);
        //删除标记，0未删除，1删除
        wrapper.eq(Supplier::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    @Override
    public boolean delById(Long supplierId) {
        Supplier supplier = this.getById(supplierId);
        //是否启用,0停用,1启用
        if(supplier.getEnabled() == 1){
            throw new BusinessException("启用状态不能删除");
        }
        //删除标记，0未删除，1删除
        supplier.setDeleteFlag(1);
        return this.updateById(supplier);
    }

    @Override
    public boolean setState(Long supplierId, Integer enabled) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        //启用 0-禁用  1-启用
        supplier.setEnabled(enabled);
        if(supplier.getEnabled() == 0){
            if(this.countHesdBySupplierId(supplierId) > 0){
                throw new BusinessException("供应商已产生单据，不能停用");
            }
        }
        return this.updateById(supplier);
    }

    //查询供应商产生单据数量
    public Integer countHesdBySupplierId(Long supplierId) {
        return billHeadService.count(new LambdaQueryWrapper<BillHead>()
                .eq(BillHead::getSupplierId, supplierId)
                .eq(BillHead::getDeleteFlag, 0)
        );
    }

    @Override
    public Map<String, Object> toEdit(Long supplierId) {
        Map<String, Object> map = new HashMap<>();
        map.put("region",regionCache.getCacheTree());
        Supplier supplier = this.getById(supplierId);
        if(StringUtils.isNotEmpty(supplier.getImageUrl())){
            supplier.setImageList(Arrays.asList(supplier.getImageUrl().split(",")));
        }
        map.put("model", supplier);
        return map;
    }

    //供应商列表
    @Override
    public List<Supplier> getAll() {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Supplier::getDeleteFlag, 0);
        wrapper.orderByAsc(Supplier::getSort);
        wrapper.orderByDesc(Supplier::getCreateTime);
        return this.list(wrapper);
    }
}
