package net.jjjerp.common.enums;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum DeliveryStatuEnum {
    NO("未发货", 10),
    ALL("全部已发货", 20),
    PORTION("部分发货", 30);

    private String name;
    private Integer value;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        DeliveryStatuEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryStatuEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    // 查找value集合
    public static Map<Integer, JSONObject> getList() {
        Map<Integer, JSONObject> values = new HashMap<>();
        DeliveryStatuEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryStatuEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            values.put(item.getValue(), obj);
        }
        return values;
    }

    public static List<JSONObject> getDeliveryList() {
        List<JSONObject> list = new ArrayList<>();
        DeliveryStatuEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryStatuEnum item : enums) {
            JSONObject obj = new JSONObject();
            obj.put("name", item.getName());
            obj.put("value", item.getValue());
            list.add(obj);
        }
        return list;
    }

    //查找name
    public static String getName(Integer value) {
        String name = null;
        DeliveryStatuEnum[] enums = values();    //获取所有枚举集合
        for (DeliveryStatuEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }
}
