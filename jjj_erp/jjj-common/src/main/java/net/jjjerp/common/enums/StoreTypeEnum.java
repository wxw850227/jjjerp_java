package net.jjjerp.common.enums;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺类型枚举
 */
@Getter
@AllArgsConstructor
public enum StoreTypeEnum {
    OTHER("其他平台", 1),
    OFFLINE("线下门店", 2);

    private String name;
    private Integer value;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        StoreTypeEnum[] enums = values();    //获取所有枚举集合
        for (StoreTypeEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    // 查找value集合
    public static Map<Integer, JSONObject> getList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        StoreTypeEnum[] enums = values();    //获取所有枚举集合
        for (StoreTypeEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    //查找name
    public static String getName(Integer value) {
        String name = null;
        StoreTypeEnum[] enums = values();    //获取所有枚举集合
        for (StoreTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
