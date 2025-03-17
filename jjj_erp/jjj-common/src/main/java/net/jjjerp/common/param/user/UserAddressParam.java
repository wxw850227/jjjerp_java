package net.jjjerp.common.param.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.jjjerp.common.util.excel.ExcelImport;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserAddressParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer addressId;

    @ApiModelProperty("收货人姓名")
    @NotBlank(message = "收货人不能为空")
    private String name;

    @ApiModelProperty("手机")
    @NotBlank(message = "手机不能为空")
    private String phone;

    @ApiModelProperty("固话")
    private String fixPhone;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("邮编")
    private String zip;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在区id")
    private Integer regionId;

    @ApiModelProperty("新市辖区(该字段用于记录region表中没有的市辖区)")
    private String district;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("用户id")
    private Integer userId;

}


