

package net.jjjerp.config.constant;

/**
 *  redis key 常量
 **/
public interface CommonRedisKey {

    // --------------------admin端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String ADMIN_LOGIN_TOKEN = "erp.admin:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String ADMIN_LOGIN_USER = "erp.admin:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String ADMIN_LOGIN_SALT = "erp.admin:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String ADMIN_LOGIN_USER_TOKEN = "erp.admin:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String ADMIN_LOGIN_USER_ALL_TOKEN = "erp.admin:login:user:token:%s:*";

    // --------------------admin端登录信息结束----------------------

    // --------------------shop端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String SHOP_LOGIN_TOKEN = "erp.shop:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String SHOP_LOGIN_USER = "erp.shop:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String SHOP_LOGIN_SALT = "erp.shop:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String SHOP_LOGIN_USER_TOKEN = "erp.shop:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String SHOP_LOGIN_USER_ALL_TOKEN = "erp.shop:login:user:token:%s:*";

    // --------------------shop端登录信息结束----------------------

    /**
     * 地区缓存
     */
    String REGION_DATA = "erp.region.data";
    /**
     * 设置缓存
     */
    String SETTING_DATA = "erp.setting.data:%s:%s";
    /**
     * 所有设置缓存
     */
    String SETTING_DATA_ALL = "erp.setting.data:%s:*";
    /**
     * 商品分类缓存
     */
    String PRODUCT_CATEGORY_DATA = "erp.product.category.data:%s";

    /**
     * uat测试安全ip
     */
    String UAT_IP = "uat.ip";
    /**
     * 所有设置缓存
     */
    String TASK_DATA= "erp.task.data.%s:%s:%s:%s";
}
