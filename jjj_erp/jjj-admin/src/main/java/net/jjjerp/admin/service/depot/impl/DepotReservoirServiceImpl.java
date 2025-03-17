package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.mapper.depot.DepotReservoirMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.depot.DepotAreaService;
import net.jjjerp.admin.service.depot.DepotReservoirService;
import net.jjjerp.admin.service.depot.DepotShelfService;
import net.jjjerp.admin.service.depot.DepotStorageService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库库区表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotReservoirServiceImpl extends BaseServiceImpl<DepotReservoirMapper, DepotReservoir> implements DepotReservoirService {

    @Autowired
    private DepotReservoirMapper depotReservoirMapper;
    @Autowired
    private DepotShelfService shelfService;
    @Autowired
    private DepotStorageService storageService;
    @Autowired
    private DepotAreaService areaService;

    @Override
    public List<DepotReservoir> getList(CommonPageParam param) {
        // 仓库库区列表
        LambdaQueryWrapper<DepotReservoir> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(DepotReservoir::getStatus, 1);
        if(param.getAreaId() == null){
            throw new BusinessException("区域id不能为空");
        }
        //区域id
        wrapper.eq(DepotReservoir::getAreaId, param.getAreaId());
        wrapper.orderByDesc(DepotReservoir::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public boolean add(DepotReservoir reservoir) {
        if(reservoir.getAreaId() == null){
            throw new BusinessException("区域id不能为空");
        }
        DepotArea area = areaService.getById(reservoir.getAreaId());
        if(this.checkIsNameExist(null,area.getDepotId(), reservoir.getReservoirName()) > 0){
            throw new BusinessException("名称已存在");
        }
        if(StringUtils.isNotEmpty(reservoir.getReservoirCode())){
            if(this.checkIsCodeExist(null,area.getDepotId(), reservoir.getReservoirCode()) > 0){
                throw new BusinessException("编码已存在");
            }
        }
        reservoir.setDepotId(area.getDepotId());
        return this.save(reservoir);
    }

    public int checkIsNameExist(Integer id,Long depotId, String name) {
        LambdaQueryWrapper<DepotReservoir> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotReservoir::getDepotReservoirId, id);
        }
        wrapper.eq(DepotReservoir::getReservoirName, name);
        wrapper.eq(DepotReservoir::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotReservoir::getStatus, 1);
        return this.count(wrapper);
    }

    public int checkIsCodeExist(Integer id,Long depotId, String code) {
        LambdaQueryWrapper<DepotReservoir> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotReservoir::getDepotReservoirId, id);
        }
        wrapper.eq(DepotReservoir::getReservoirCode, code);
        wrapper.eq(DepotReservoir::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotReservoir::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(DepotReservoir reservoir) {
        if(this.checkIsNameExist(reservoir.getDepotReservoirId(),reservoir.getDepotId(), reservoir.getReservoirName()) > 0){
            throw new BusinessException("名称已存在");
        }
        if(this.checkIsCodeExist(reservoir.getDepotReservoirId(),reservoir.getDepotId(), reservoir.getReservoirCode()) > 0){
            throw new BusinessException("编码已存在");
        }
        return this.updateById(reservoir);
    }

    @Override
    public boolean delById(Integer depotReservoirId) {
        //货架List
        List<DepotShelf> shelfList = shelfService.list(new LambdaQueryWrapper<DepotShelf>()
                .eq(DepotShelf::getReservoirId,depotReservoirId)
                //是否显示1显示0隐藏
                .eq(DepotShelf::getStatus,1)
        );
        if(CollectionUtils.isNotEmpty(shelfList)){
            throw new BusinessException("库区不是空的, 请先删除库区下的货架后再重试");
        }
        //货位List
        List<DepotStorage> storageList = storageService.list(new LambdaQueryWrapper<DepotStorage>()
                .eq(DepotStorage::getReservoirId,depotReservoirId)
                //是否显示1显示0隐藏
                .eq(DepotStorage::getStatus,1)
        );
        if(CollectionUtils.isNotEmpty(storageList)){
            throw new BusinessException("库区不是空的, 请先删除库区下的货位后再重试");
        }
        DepotReservoir reservoir= new DepotReservoir();
        reservoir.setDepotReservoirId(depotReservoirId);
        //是否显示1显示0隐藏
        reservoir.setStatus(0);
        return this.updateById(reservoir);
    }
}
