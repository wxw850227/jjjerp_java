package net.jjjerp.common.service.settings.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.settings.Region;
import net.jjjerp.common.mapper.settings.RegionMapper;
import net.jjjerp.common.service.settings.RegionService;
import net.jjjerp.common.vo.RegionVo;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-24
 */
@Slf4j
@Service
public class RegionServiceImpl extends BaseServiceImpl<RegionMapper, Region> implements RegionService {

    @Autowired
    private RegionCache regionCache;

    public List<JSONArray> getRegionForApi() {
        List<JSONArray> result = new ArrayList<>();
        JSONArray provinceList = new JSONArray();
        JSONArray cityList = new JSONArray();
        JSONArray areaList = new JSONArray();
        JSONObject json = null;
        // 省份
        for (RegionVo province : regionCache.getCacheTree()) {
            json = new JSONObject();
            json.put("label", province.getName());
            json.put("value", province.getId());
            provinceList.add(json);

            JSONArray cityListTemp = new JSONArray();
            JSONArray cityAreaListTemp = new JSONArray();
            // 城市
            for(RegionVo city : province.getChildren()){
                json = new JSONObject();
                json.put("label", city.getName());
                json.put("value", city.getId());
                cityListTemp.add(json);

                JSONArray areaListTemp = new JSONArray();
                // 地区
                for(RegionVo area : city.getChildren()){
                    json = new JSONObject();
                    json.put("label", area.getName());
                    json.put("value", area.getId());
                    areaListTemp.add(json);
                }
                cityAreaListTemp.add(areaListTemp);
            }
            if(CollectionUtils.isNotEmpty(cityAreaListTemp)){
                areaList.add(cityAreaListTemp);
            }
            if(CollectionUtils.isNotEmpty(cityListTemp)){
                cityList.add(cityListTemp);
            }
        }
        result.add(provinceList);
        result.add(cityList);
        result.add(areaList);
        return result;
    }

}
