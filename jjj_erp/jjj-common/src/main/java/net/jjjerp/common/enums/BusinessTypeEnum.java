package net.jjjerp.common.enums;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据类型枚举
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {
    //业务类型,1直接采购入库,2订单采购入库,3直接出库,4采购入库单退货,5实际调拨,6虚拟调拨,7销售单原路退回,8线下销售订单,
    // 9线上销售订单,10预售销售订单,11线下出库单

    ZJCGRK("直接采购入库", 1,"ZJCGRK"),
    DDCGRK("订单采购入库", 2,"DDCGRK"),
    ZJCK("直接出库", 3,"ZJCK"),
    CGRKDTH("采购入库单退货", 4,"CGRKDTH"),
    SJDB("实际调拨", 5,"SJDB"),
    XNDB("虚拟调拨", 6,"XNDB"),
    XTYLTH("销售退货单-原路退回", 7,"XTYLTH"),
    XXXSDD("线下销售订单", 8,"XXXSDD"),
    XSXSDD("线上销售订单", 9,"XSXSDD"),
    YSXSDD("预售销售订单", 10,"YSXSDD"),
    XXCKD("线下出库单", 11,"XXCKD");

    private String name;
    private Integer value;
    private String code;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        BusinessTypeEnum[] enums = values();    //获取所有枚举集合
        for (BusinessTypeEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }


    public static List<JSONObject> getList() {
        List<JSONObject> list = new ArrayList<>();
        BusinessTypeEnum[] enums = values();    //获取所有枚举集合
        for (BusinessTypeEnum item : enums) {
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
        BusinessTypeEnum[] enums = values();    //获取所有枚举集合
        for (BusinessTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                name = item.getName();
                break;
            }
        }
        return name;
    }

    //查找code
    public static String getCode(Integer value) {
        String code = null;
        BusinessTypeEnum[] enums = values();    //获取所有枚举集合
        for (BusinessTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                code = item.getCode();
                break;
            }
        }
        return code;
    }
}
