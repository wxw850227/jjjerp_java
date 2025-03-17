

package net.jjjerp.admin.controller.bill;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.common.entity.bill.BillAddress;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.util.SettingUtils;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"单据通用接口"})
@RestController
@RequestMapping("/admin/bill")
public class BillController {
    @Autowired
    private BillHeadService billHeadService;

    @RequestMapping(value = "/editAddress", method = RequestMethod.POST)
    @OperationLog(name = "修改收货地址")
    @ApiOperation(value = "修改收货地址", response = String.class)
    public ApiResult<String> editAddress(@RequestBody @Validated BillAddress address) {
        if(billHeadService.editAddress(address)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }

    @RequestMapping(value = "/editRemark", method = RequestMethod.POST)
    @OperationLog(name = "修改单据备注信息")
    @ApiOperation(value = "修改单据备注信息", response = String.class)
    public ApiResult<String> editRemark(@RequestBody @Validated BillHead head) {
        if(billHeadService.editRemark(head)) {
            return ApiResult.ok(null, "操作成功");
        }else{
            return ApiResult.fail("操作失败");
        }
    }
}
