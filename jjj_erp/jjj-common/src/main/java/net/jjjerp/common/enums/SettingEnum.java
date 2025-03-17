package net.jjjerp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum SettingEnum {
    /**
     * 系统设置
     **/
    SYS_CONFIG("sys_config", "admin系统设置", "SysConfigVo"),
    STORE("store", "shop系统设置", "StoreVo"),
    STORAGE("storage", "上传设置", "StorageVo"),
    SMS("sms", "短信设置", "SmsVo"),
    PRINTING("printer", "打印机设置", "PrinterVo");
    private String key;
    private String description;
    private String className;

    //根据传入的key动态获取className
    public static String getClassNameByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getClassName();
            }
        }
        return null;
    }

    //根据传入的key动态获取description
    public static String getDescriptionByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getDescription();
            }
        }
        return null;
    }
}
