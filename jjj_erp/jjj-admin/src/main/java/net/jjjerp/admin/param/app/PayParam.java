package net.jjjerp.admin.param.app;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "支付方式参数", description = "支付方式参数")
public class PayParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付类型，json格式")
    private JSONObject payType;

    @ApiModelProperty("微信支付方式v2：2,v3：3")
    private Integer wxPayKind;

    @ApiModelProperty("微信提现方式v2:2 v3:3")
    private Integer wxCashType;

    @ApiModelProperty("支付宝提现方式，1=手动打款， 2=审核通过自动转账")
    private Integer aliCashType;

    @ApiModelProperty("微信商户号id")
    private String mchid;

    @ApiModelProperty("微信支付密钥")
    private String apikey;

    @ApiModelProperty("证书文件cert")
    private String certPem;

    @ApiModelProperty("证书文件key")
    private String keyPem;

    @ApiModelProperty("支付宝商户号")
    private String alipayAppid;

    @ApiModelProperty("支付宝公钥")
    private String alipayPublickey;

    @ApiModelProperty("应用私钥")
    private String alipayPrivatekey;
}
