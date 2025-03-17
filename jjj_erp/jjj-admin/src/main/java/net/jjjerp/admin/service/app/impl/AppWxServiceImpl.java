package net.jjjerp.admin.service.app.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.app.AppWx;
import net.jjjerp.common.mapper.app.AppWxMapper;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.bean.RequestDetail;
import net.jjjerp.framework.core.util.RequestDetailThreadLocal;
import net.jjjerp.admin.service.app.AppWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 微信小程序记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-03
 */
@Slf4j
@Service
public class AppWxServiceImpl extends BaseServiceImpl<AppWxMapper, AppWx> implements AppWxService {
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    /**
     * 修改
     * @param wxappId
     * @param wxappSecret
     * @return
     */
    public Boolean edit(String wxappId, String wxappSecret){
        AppWx app = this.getOne(new LambdaQueryWrapper<>());
        AppWx updateBean = new AppWx();
        updateBean.setWxappId(wxappId);
        updateBean.setWxappSecret(wxappSecret);
        // 删除缓存
        try{
            wxMaService.removeConfig(wxappId);
            // 旧的也删除
            if(!wxappId.equals(app.getWxappId())){
                wxMaService.removeConfig(app.getWxappId());
            }
        }catch (Exception e){
            log.info("保存设置删除缓存错误");
        }
        if(app == null){
            return this.save(updateBean);
        }else{
            return this.update(updateBean, new LambdaQueryWrapper<>());
        }
    }
}
