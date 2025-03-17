package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.mapper.depot.DepotShelfMapper;
import net.jjjerp.common.param.CommonPageParam;
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

/**
 * 仓库货架表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotShelfServiceImpl extends BaseServiceImpl<DepotShelfMapper, DepotShelf> implements DepotShelfService {

    @Autowired
    private DepotShelfMapper depotShelfMapper;
    @Autowired
    private DepotReservoirService reservoirService;
    @Autowired
    private DepotStorageService storageService;

    @Override
    public List<DepotShelf> getList(CommonPageParam param) {
        // 仓库货架列表
        LambdaQueryWrapper<DepotShelf> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(DepotShelf::getStatus, 1);
        if(param.getReservoirId() == null){
            throw new BusinessException("库区id不能为空");
        }
        //库区id
        wrapper.eq(DepotShelf::getReservoirId, param.getReservoirId());
        wrapper.orderByDesc(DepotShelf::getCreateTime);
        List<DepotShelf> list = this.list(wrapper);
        for(DepotShelf shelf: list){
            DepotReservoir reservoir = reservoirService.getById(shelf.getReservoirId());
            if(reservoir != null){
                shelf.setReservoirName(reservoir.getReservoirName());
            }
        }
        return list;
    }

    @Override
    public boolean add(DepotShelf shelf) {
        if(shelf.getReservoirId() == null){
            throw new BusinessException("库区id不能为空");
        }
        DepotReservoir reservoir = reservoirService.getById(shelf.getReservoirId());
        if(this.checkIsNameExist(null,reservoir.getDepotId(), shelf.getShelfName()) > 0){
            throw new BusinessException("名称已存在");
        }
        if(StringUtils.isNotEmpty(reservoir.getReservoirCode())){
            if(this.checkIsCodeExist(null,reservoir.getDepotId(), shelf.getShelfCode()) > 0){
                throw new BusinessException("编码已存在");
            }
        }
        shelf.setDepotId(reservoir.getDepotId());
        return this.save(shelf);
    }

    public int checkIsNameExist(Integer id,Long depotId, String name) {
        LambdaQueryWrapper<DepotShelf> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotShelf::getDepotShelfId, id);
        }
        wrapper.eq(DepotShelf::getShelfName, name);
        wrapper.eq(DepotShelf::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotShelf::getStatus, 1);
        return this.count(wrapper);
    }

    public int checkIsCodeExist(Integer id,Long depotId, String code) {
        LambdaQueryWrapper<DepotShelf> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotShelf::getDepotShelfId, id);
        }
        wrapper.eq(DepotShelf::getShelfCode, code);
        wrapper.eq(DepotShelf::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotShelf::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(DepotShelf shelf) {
        if(this.checkIsNameExist(shelf.getDepotShelfId(),shelf.getDepotId(), shelf.getShelfName()) > 0){
            throw new BusinessException("名称已存在");
        }
        if(this.checkIsCodeExist(shelf.getDepotShelfId(),shelf.getDepotId(), shelf.getShelfCode()) > 0){
            throw new BusinessException("编码已存在");
        }
        return this.updateById(shelf);
    }

    @Override
    public boolean delById(Integer depotShelfId) {
        //货位List
        List<DepotStorage> storageList = storageService.list(new LambdaQueryWrapper<DepotStorage>()
                .eq(DepotStorage::getSheifId,depotShelfId)
                //是否显示1显示0隐藏
                .eq(DepotStorage::getStatus,1)
        );
        if(CollectionUtils.isNotEmpty(storageList)){
            throw new BusinessException("货架不是空的, 请先删除货架下的货位后再重试");
        }
        DepotShelf shelf= new DepotShelf();
        shelf.setDepotShelfId(depotShelfId);
        //是否显示1显示0隐藏
        shelf.setStatus(0);
        return this.updateById(shelf);
    }
}
