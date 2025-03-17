package net.jjjerp.common.enums;


import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单据类型枚举
 */
@Getter
@AllArgsConstructor
public enum BillTypeEnum {
    //单据类型,1采购订单,2采购入库单,3采购退货订单,4采购退货单,6销售订单,8销售出库单,10库存调拨单,11调拨出库单,12调拨入库单,13其他出库单,14其他入库单,15盘盈盘亏单,16期初库存单,17售后申请单,18销售退货单

    CGDD("采购订单", 1,"CGDD"),
    CGRKD("采购入库单", 2,"CGRKD"),
//    CGTHDD("采购退货订单", 3,"CGTHDD"),
    CGTHD("采购退货单", 4,"CGTHD"),
    XSDD("销售订单", 6,"XSDD"),
    XSCKD("销售出库单", 8,"XSCKD"),
    KCDBD("库存调拨单", 10,"KCTBD"),
    DBCKD("调拨出库单", 11,"TBCKD"),
    DBRKD("调拨入库单", 12,"TBRKD"),
    QTCKD("其他出库单", 13,"QTCKD"),
    QTRKD("其他入库单", 14,"QTRKD"),
    PYPKD("盘盈盘亏单", 15,"PYPKD"),
    QCKCD("期初库存单", 16,"QCKCD"),
    SHSQD("售后申请单", 17,"SHSQD"),
    XSTHD("销售退货单", 18,"XSTHD");

    private String name;
    private Integer value;
    private String code;

    //查找value集合
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
            values.add(item.getValue());
        }
        return values;
    }

    //获取出入库类型集合
    public static List<Integer> getStockValues() {
        List<Integer> values = new ArrayList<>();
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
            if("采购入库单".equals(item.getName()) || "销售出库单".equals(item.getName()) || "线下出库单".equals(item.getName())
             || "调拨出库单".equals(item.getName()) || "其他出库单".equals(item.getName()) || "盘盈盘亏单".equals(item.getName())
                    || "采购退货单".equals(item.getName()) || "其他入库单".equals(item.getName()) || "调拨入库单".equals(item.getName())
                    || "期初库存单".equals(item.getName()) || "销售退货单".equals(item.getName())
            ){
                values.add(item.getValue());
            }

        }
        return values;
    }

    //获取出库类型集合
    public static List<Integer> getOutValues() {
        List<Integer> values = new ArrayList<>();
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
            if("销售出库单".equals(item.getName()) || "线下出库单".equals(item.getName())
                    || "调拨出库单".equals(item.getName()) || "其他出库单".equals(item.getName())
                    || "采购退货单".equals(item.getName())
            ){
                values.add(item.getValue());
            }

        }
        return values;
    }

    //获取入库类型集合
    public static List<Integer> getFoldValues() {
        List<Integer> values = new ArrayList<>();
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
            if("采购入库单".equals(item.getName()) || "期初库存单".equals(item.getName())
                    || "其他入库单".equals(item.getName()) || "调拨入库单".equals(item.getName())
                    || "盘盈盘亏单".equals(item.getName()) || "销售退货单".equals(item.getName())
            ){
                values.add(item.getValue());
            }

        }
        return values;
    }


    public static List<JSONObject> getList() {
        List<JSONObject> list = new ArrayList<>();
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
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
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
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
        BillTypeEnum[] enums = values();    //获取所有枚举集合
        for (BillTypeEnum item : enums) {
            if(item.getValue().intValue() == value.intValue()){
                code = item.getCode();
                break;
            }
        }
        return code;
    }
}
