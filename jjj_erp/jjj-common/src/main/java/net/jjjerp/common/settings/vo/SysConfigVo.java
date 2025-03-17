package net.jjjerp.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class SysConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shopName;
    private String shopBgImg;
    private WeixinService weixinService;
    //上传设置VO
    private StorageVo storageVo;

    @Data
    @Accessors(chain = true)
    @ApiModel("系统设置VO")
    public static class WeixinService implements Serializable{
        private static final long serialVersionUID = 1L;
        private Boolean isOpen;
        private String appId;
        private String mchId;
        private String apiKey;
        private String certPem;
        private String keyPem;
        public WeixinService(){
            this.isOpen = false;
            this.appId = "";
            this.mchId = "";
            this.apiKey = "";
            this.certPem = "";
            this.keyPem = "";
        }
    }

    public SysConfigVo(){
        this.shopName = "三勾商城管理系统";
        this.shopBgImg = "";
        this.weixinService = new WeixinService();
        this.storageVo = new StorageVo();
    }
}
