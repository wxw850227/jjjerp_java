package net.jjjerp.admin.service.app;

import net.jjjerp.common.entity.app.App;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.admin.param.app.PayParam;
import net.jjjerp.admin.vo.app.AppVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 微信小程序记录表 服务类
 * @author jjjerp
 * @since 2022-06-23
 */
public interface AppService extends BaseService<App> {

    /**
     * 修改app
     * @param passportType
     * @return
     */
    Boolean edit(Integer passportType);

    /**
     * 详情
     * @param
     * @return
     */
    AppVo detail();

    /**
     * 修改支付设置
     * @param payParam
     * @return
     */
    Boolean editPay(PayParam payParam);

    /**
     * 保存p12证书
     * @param multipartFile
     * @throws Exception
     */
    Boolean uploadP12(MultipartFile multipartFile);
}
