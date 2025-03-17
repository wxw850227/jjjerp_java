package net.jjjerp.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjerp.common.enums.DeliveryTypeEnum;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class StoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> deliveryType;
    private Boolean isGetLog;
    private KuaiDi100 kuaiDi100;
    private Policy policy;

    @Data
    @Accessors(chain = true)
    @ApiModel("快递100VO")
    public static class KuaiDi100 implements Serializable{
        private static final long serialVersionUID = 1L;
        //授权key
        private String key;
        //查询customer
        private String customer;
        //电子面单secret
        private String secret;

        public KuaiDi100(){
            this.customer = "";
            this.key = "";
            this.secret = "";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("隐私政策")
    public static class Policy implements Serializable{
        private static final long serialVersionUID = 1L;
        private String service;
        private String privacy;
        public Policy(){
            this.service = "";
            this.privacy = "";
        }
    }

    public StoreVo(){
        this.deliveryType = DeliveryTypeEnum.getValues();
        this.isGetLog = false;
        this.kuaiDi100 = new KuaiDi100();
        this.policy = new Policy();
    }
}
