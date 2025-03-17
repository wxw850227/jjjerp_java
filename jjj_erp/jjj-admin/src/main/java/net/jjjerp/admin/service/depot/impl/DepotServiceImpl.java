package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.entity.supplier.Supplier;
import net.jjjerp.common.mapper.depot.DepotMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DepotService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 仓库表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotServiceImpl extends BaseServiceImpl<DepotMapper, Depot> implements DepotService {

    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BillHeadService billHeadService;

    @Override
    public Paging<Depot> getList(CommonPageParam param) {
        // 仓库列表
        Page<Depot> page = new PageInfo<>(param);
        LambdaQueryWrapper<Depot> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Depot::getDeleteFlag, 0);
        //编码搜索
        if(StringUtils.isNotEmpty(param.getCode())){
            wrapper.like(Depot::getCode, param.getCode());
        }
        //名称搜索
        if(StringUtils.isNotEmpty(param.getName())){
            wrapper.like(Depot::getName, param.getName());
        }
        // 启用 0-禁用  1-启用
        if(param.getEnabled() != null && param.getEnabled() > -1){
            wrapper.eq(Depot::getEnabled, param.getEnabled());
        }
        //仓库类型,0正品仓,1次品仓,2门店仓
        if(param.getType() != null && param.getType() > -1){
            wrapper.eq(Depot::getType, param.getType());
        }
        wrapper.orderByDesc(Depot::getCreateTime);
        IPage<Depot> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        return new Paging<>(iPage);
    }

    @Override
    public boolean add(Depot depot) {
        if(this.checkIsNameExist(null, depot.getName()) > 0){
            throw new BusinessException("仓库名称已存在");
        }
        if(StringUtils.isNotEmpty(depot.getCode())){
            if(this.checkIsCodeExist(null, depot.getCode()) > 0){
                throw new BusinessException("仓库编码已存在");
            }
        }
        return this.save(depot);
    }

    public int checkIsNameExist(Long id, String name) {
        LambdaQueryWrapper<Depot> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Depot::getDepotId, id);
        }
        wrapper.eq(Depot::getName, name);
        //删除标记，0未删除，1删除
        wrapper.eq(Depot::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    public int checkIsCodeExist(Long id, String code) {
        LambdaQueryWrapper<Depot> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Depot::getDepotId, id);
        }
        wrapper.eq(Depot::getCode, code);
        //删除标记，0未删除，1删除
        wrapper.eq(Depot::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(Depot depot) {
        if(this.checkIsNameExist(depot.getDepotId(), depot.getName()) > 0){
            throw new BusinessException("仓库名称已存在");
        }
        if(StringUtils.isNotEmpty(depot.getCode())){
            if(this.checkIsCodeExist(depot.getDepotId(), depot.getCode()) > 0){
                throw new BusinessException("仓库编码已存在");
            }
        }
        return this.updateById(depot);
    }

    @Override
    public boolean setState(Long depotId, Integer enabled) {
        Depot depot = new Depot();
        depot.setDepotId(depotId);
        //启用 0-禁用  1-启用
        depot.setEnabled(enabled);
        if(depot.getEnabled() == 0){
            if(this.countHesdByDepotId(depotId) > 0){
                throw new BusinessException("仓库已产生单据，不能停用");
            }
        }
        return this.updateById(depot);
    }

    public Integer countHesdByDepotId(Long depotId) {
        return billHeadService.count(new LambdaQueryWrapper<BillHead>()
                .eq(BillHead::getDepotId, depotId)
                .eq(BillHead::getDeleteFlag, 0)
        );
    }

    @Override
    public boolean delById(Long depotId) {
        Depot depot = this.getById(depotId);
        //已经停用能够删除
        if(depot.getEnabled() == 1){
            throw new BusinessException("启用状态不能删除");
        }
        //删除标记，0未删除，1删除
        depot.setDeleteFlag(1);
        return this.updateById(depot);
    }

    //仓库列表
    @Override
    public List<Depot> getAll() {
        LambdaQueryWrapper<Depot> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Depot::getDeleteFlag, 0);
        wrapper.orderByDesc(Depot::getCreateTime);
        return this.list(wrapper);
    }
}
