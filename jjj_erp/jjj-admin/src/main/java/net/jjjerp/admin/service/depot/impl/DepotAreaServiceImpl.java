package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.mapper.depot.DepotAreaMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DepotAreaService;
import net.jjjerp.admin.service.depot.DepotReservoirService;
import net.jjjerp.admin.service.depot.DepotShelfService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库区域表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotAreaServiceImpl extends BaseServiceImpl<DepotAreaMapper, DepotArea> implements DepotAreaService {

    @Autowired
    private DepotAreaMapper depotAreaMapper;
    @Autowired
    private DepotReservoirService reservationService;
    @Autowired
    private DepotShelfService shelfService;

    @Override
    public List<DepotArea> getList(CommonPageParam param) {
        // 仓库区域列表
        LambdaQueryWrapper<DepotArea> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(DepotArea::getStatus, 1);
        if(param.getDepotId() == null){
            throw new BusinessException("仓库id不能为空");
        }
        //仓库id
        wrapper.eq(DepotArea::getDepotId, param.getDepotId());
        wrapper.orderByDesc(DepotArea::getCreateTime);
        List<DepotArea> list = this.list(wrapper);
        list = list.stream().map(this::transVo).collect(Collectors.toList());
        return list;
    }

    public DepotArea transVo(DepotArea area) {
        //库区List
        area.setReservoirList(reservationService.list(new LambdaQueryWrapper<DepotReservoir>()
                .eq(DepotReservoir::getAreaId,area.getDepotAreaId())
                //是否显示1显示0隐藏
                .eq(DepotReservoir::getStatus,1)
        ));
        if(CollectionUtils.isNotEmpty(area.getReservoirList())){
            for(DepotReservoir reservation : area.getReservoirList()){
                //货架List
                reservation.setShelfList(shelfService.list(new LambdaQueryWrapper<DepotShelf>()
                        .eq(DepotShelf::getReservoirId,reservation.getDepotReservoirId())
                        //是否显示1显示0隐藏
                        .eq(DepotShelf::getStatus,1)
                ));
            }
        }
        return area;
    }

    @Override
    public boolean add(DepotArea area) {
        if(this.checkIsNameExist(null,area.getDepotId(), area.getAreaName()) > 0){
            throw new BusinessException("区域名称已存在");
        }
        if(StringUtils.isNotEmpty(area.getAreaCode())){
            if(this.checkIsCodeExist(null,area.getDepotId(), area.getAreaCode()) > 0){
                throw new BusinessException("区域编码已存在");
            }
        }
        return this.save(area);
    }

    public int checkIsNameExist(Integer id,Long depotId, String name) {
        LambdaQueryWrapper<DepotArea> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotArea::getDepotAreaId, id);
        }
        wrapper.eq(DepotArea::getAreaName, name);
        wrapper.eq(DepotArea::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotArea::getStatus, 1);
        return this.count(wrapper);
    }

    public int checkIsCodeExist(Integer id,Long depotId, String code) {
        LambdaQueryWrapper<DepotArea> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotArea::getDepotAreaId, id);
        }
        wrapper.eq(DepotArea::getAreaCode, code);
        wrapper.eq(DepotArea::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotArea::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(DepotArea area) {
        if(this.checkIsNameExist(area.getDepotAreaId(),area.getDepotId(), area.getAreaName()) > 0){
            throw new BusinessException("区域名称已存在");
        }
        if(this.checkIsCodeExist(area.getDepotAreaId(),area.getDepotId(), area.getAreaCode()) > 0){
            throw new BusinessException("区域编码已存在");
        }
        return this.updateById(area);
    }

    @Override
    public boolean delById(Integer depotAreaId) {
        //库区List
        List<DepotReservoir> reservoirList = reservationService.list(new LambdaQueryWrapper<DepotReservoir>()
                .eq(DepotReservoir::getAreaId,depotAreaId)
                //是否显示1显示0隐藏
                .eq(DepotReservoir::getStatus,1)
        );
        if(CollectionUtils.isNotEmpty(reservoirList)){
            throw new BusinessException("区域不是空的, 请先删除区域下的库区后再重试");
        }
        DepotArea area = new DepotArea();
        area.setDepotAreaId(depotAreaId);
        //是否显示1显示0隐藏
        area.setStatus(0);
        return this.updateById(area);
    }
}
