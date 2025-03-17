package net.jjjerp.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("打印机设置VO")
public class PrinterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer isOpen;
    private Integer orderStatus;
    private Integer printerId;

    //电子面单打印方式，0本地打印，1快递100云打印
    private Integer labelPrintType;
    //打印机ID
    private Integer labelPrinterId;
    //快递100云打印机编码
    private String siid;

    public PrinterVo() {
        this.isOpen = 1;
        this.orderStatus = 20;
        this.printerId = 0;

        this.labelPrintType = 0;
        this.labelPrinterId = 0;
        this.siid = "";
    }

}
