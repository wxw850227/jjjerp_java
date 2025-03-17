package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.jjjerp.common.entity.depot.DeliverArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.entity.settings.Region;
import net.jjjerp.common.mapper.depot.DeliverAreaMapper;
import net.jjjerp.admin.service.depot.DeliverAreaService;
import net.jjjerp.admin.service.settings.RegionService;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 配送区域表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-29
 */
@Slf4j
@Service
public class DeliverAreaServiceImpl extends BaseServiceImpl<DeliverAreaMapper, DeliverArea> implements DeliverAreaService {

    @Autowired
    private DeliverAreaMapper deliverAreaMapper;
    @Autowired
    private RegionService regionService;

    @Override
    public List<DeliverArea> getList() {
        LambdaQueryWrapper<DeliverArea> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(DeliverArea::getStatus, 1);
        wrapper.orderByDesc(DeliverArea::getCreateTime);
        List<DeliverArea> list = this.list(wrapper);
        list = list.stream().map(this::transVo).collect(Collectors.toList());
        return list;
    }

    public DeliverArea transVo(DeliverArea area){
        //暂时没有要添加的
        return area;
    }


    @Override
    public boolean add(DeliverArea area) {
        if(this.checkIsNameExist(null, area.getName()) > 0){
            throw new BusinessException("区域名称已存在");
        }
        List<Integer> provinceIdList = area.getProvinceIdList();
        area.setProvinceIds(StringUtils.join(provinceIdList, ","));
        List<String> provinceNameList = area.getProvinceNameList();
        area.setProvinceNames(StringUtils.join(provinceNameList, ","));
        return this.save(area);
    }

    private Integer  checkIsNameExist(Integer deliverAreaId, String name) {
        LambdaQueryWrapper<DeliverArea> wrapper = new LambdaQueryWrapper<>();
        if(deliverAreaId != null){
            wrapper.ne(DeliverArea::getDeliverAreaId, deliverAreaId);
        }
        wrapper.eq(DeliverArea::getName, name);
        //是否显示1显示0隐藏
        wrapper.eq(DeliverArea::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(DeliverArea area) {
        if(this.checkIsNameExist(area.getDeliverAreaId(), area.getName()) > 0){
            throw new BusinessException("区域名称已存在");
        }
        List<Integer> provinceIdList = area.getProvinceIdList();
        area.setProvinceIds(StringUtils.join(provinceIdList, ","));
        List<String> provinceNameList = area.getProvinceNameList();
        area.setProvinceNames(StringUtils.join(provinceNameList, ","));
        return this.updateById(area);
    }

    @Override
    public boolean delById(Integer deliverAreaId) {
        DeliverArea area= new DeliverArea();
        area.setDeliverAreaId(deliverAreaId);
        //是否显示1显示0隐藏
        area.setStatus(0);
        return this.updateById(area);
    }

    @Override
    public Map<String, Object> getAdd() {
        Map<String, Object> map = new HashMap<>();
        List<DeliverArea> areaList = this.list(new LambdaQueryWrapper<DeliverArea>().eq(DeliverArea::getStatus, 1));
        Set<Integer> provinceIdList = new HashSet<>();
        for(DeliverArea area : areaList){
            List<Integer> idList = Arrays.stream(area.getProvinceIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            provinceIdList.addAll(idList);
        }
        //所有已添加的省份ID
        map.put("allAddIdList", provinceIdList);
        // 所有地区
        List<Region> allList = regionService.list(new LambdaQueryWrapper<Region>()
                //层级 1 2 3 省市区县
                .eq(Region::getLevel, 1)
                .eq(Region::getIsDelete, false));
        map.put("provinceList", allList);
        return map;
    }

    @Override
    public Map<String, Object> getEdit(Integer deliverAreaId) {
        Map<String, Object> map = new HashMap<>();
        DeliverArea area= this.getById(deliverAreaId);
        if(area != null){
            List<Integer> provinceIdList = Arrays.stream(area.getProvinceIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            //已添加的省份ID
            map.put("addIdList", provinceIdList);
        }
        // 所有地区
        List<Region> allList = regionService.list(new LambdaQueryWrapper<Region>()
                //层级 1 2 3 省市区县
                .eq(Region::getLevel, 1)
                .eq(Region::getIsDelete, false));
        map.put("provinceList", allList);
        return map;
    }
}
