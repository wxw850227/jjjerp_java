package net.jjjerp.admin.controller.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.param.ImageBankPageParam;
import net.jjjerp.common.util.ImageBankUtils;
import net.jjjerp.common.vo.image.ImageBankVo;
import net.jjjerp.framework.common.api.ApiResult;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "image", tags = {"图片库列表"})
@RestController
@RequestMapping("/admin/image/bank")
public class ImageBankController {
    @Autowired
    private ImageBankUtils imageBankUtils;

    //列表
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<ImageBankVo>> index() throws Exception{
        return ApiResult.ok(imageBankUtils.index());
    }

    //分页列表
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @OperationLog(name = "list")
    @ApiOperation(value = "list", response = String.class)
    public ApiResult<Paging<ImageBankVo>> list(@RequestBody ImageBankPageParam param) throws Exception{
        return ApiResult.ok(imageBankUtils.list(param));
    }
}
