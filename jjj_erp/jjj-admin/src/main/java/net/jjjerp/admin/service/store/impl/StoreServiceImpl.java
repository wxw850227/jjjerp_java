package net.jjjerp.admin.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.enums.StoreTypeEnum;
import net.jjjerp.common.mapper.store.StoreMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.finance.capital.AccountService;
import net.jjjerp.admin.service.store.StoreService;
import net.jjjerp.common.util.CodeUtils;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商/客户信息表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-19
 */
@Slf4j
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, Store> implements StoreService {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private RegionCache regionCache;
    @Autowired
    private BillHeadService billHeadService;
    @Autowired
    private AccountService accountService;

    @Override
    public Paging<Store> getList(CommonPageParam param) {
        // 商品列表
        Page<Store> page = new PageInfo<>(param);
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Store::getDeleteFlag, 0);
        //名称搜索
        if(StringUtils.isNotEmpty(param.getName())){
            wrapper.like(Store::getStoreName, param.getName());
        }
        // 启用 0-禁用  1-启用
        if(param.getEnabled() != null && param.getEnabled() > -1){
            wrapper.eq(Store::getEnabled, param.getEnabled());
        }
        //店铺类型
        if(param.getType() != null && param.getType() > -1){
            wrapper.eq(Store::getStoreType, param.getType());
        }
        wrapper.orderByAsc(Store::getSort);
        wrapper.orderByDesc(Store::getCreateTime);
        IPage<Store> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        return new Paging<>(iPage);
    }

    @Override
    public boolean add(Store store) {
        if(StringUtils.isEmpty(store.getNickName())){
            store.setNickName(store.getStoreName());
        }else {
            if(this.checkIsNameExist(null, store.getNickName()) > 0){
                throw new BusinessException("店铺昵称已存在");
            }
        }
        return this.save(store);
    }

    public int checkIsNameExist(Long id, String nickName) {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Store::getStoreId, id);
        }
        wrapper.eq(Store::getNickName, nickName);
        //删除标记，0未删除，1删除
        wrapper.eq(Store::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    @Override
    public Map<String, Object> toEdit(Long storeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("region",regionCache.getCacheTree());
        map.put("storeType", StoreTypeEnum.getList());
        Store store = this.getById(storeId);
        map.put("model", store);
        map.put("account", accountService.getAll());
        return map;
    }

    @Override
    public boolean edit(Store store) {
        if(StringUtils.isEmpty(store.getNickName())){
            throw new BusinessException("店铺昵称不能为空");
        }else {
            if(this.checkIsNameExist(store.getStoreId(), store.getNickName()) > 0){
                throw new BusinessException("店铺昵称已存在");
            }
        }
        return this.updateById(store);
    }

    @Override
    public boolean delById(Long storeId) {
        Store store = this.getById(storeId);
        //已经停用能够删除
        if(store.getEnabled() == 1){
            throw new BusinessException("启用状态不能删除");
        }
        //删除标记，0未删除，1删除
        store.setDeleteFlag(1);
        return this.updateById(store);
    }

    @Override
    public boolean setState(Long storeId, Integer enabled) {
        Store store = new Store();
        store.setStoreId(storeId);
        //启用 0-禁用  1-启用
        store.setEnabled(enabled);
        if(store.getEnabled() == 0){
            List<BillHead> billHeads = billHeadService.list(new LambdaQueryWrapper<BillHead>()
                    .eq(BillHead::getStoreId, storeId)
                    //删除标记，0未删除，1删除
                    .eq(BillHead::getDeleteFlag, 0)
            );
            if(CollectionUtils.isNotEmpty(billHeads)){
                throw new BusinessException("店铺存在销售单，不能停用");
            }
        }
        return this.updateById(store);
    }

    //店铺列表
    @Override
    public List<Store> getAll() {
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Store::getDeleteFlag, 0);
        wrapper.orderByDesc(Store::getCreateTime);
        return this.list(wrapper);
    }
}
