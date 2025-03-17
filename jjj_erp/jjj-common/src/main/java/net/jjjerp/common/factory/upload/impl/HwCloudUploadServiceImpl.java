package net.jjjerp.common.factory.upload.impl;


import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.enums.SettingEnum;
import net.jjjerp.common.factory.upload.UploadFactoryService;
import net.jjjerp.common.settings.vo.StorageVo;
import net.jjjerp.common.util.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * 文件华为云上传类
 */
@Slf4j
@Service
public class HwCloudUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 华为云OBS文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile file, String saveFileName) throws Exception{
        StorageVo storageVo;
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 请求路径 /api/saas/file/upload/image
        String path = request.getRequestURI();
        if(path.startsWith("/api/saas/file/upload/image")){
            //获取admin端配置
            JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), 0L);
            JSONObject storage = vo.getJSONObject("storageVo");
            storageVo = JSONObject.toJavaObject(storage, StorageVo.class);
        }else {
            //获取shop端配置
            JSONObject vo = settingUtils.getSetting(SettingEnum.STORAGE.getKey(), null);
            storageVo = JSONObject.toJavaObject(vo, StorageVo.class);
        }

        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(storageVo.getHwCloud().getAccessKeyId(), storageVo.getHwCloud().getAccessKeySecret(), storageVo.getHwCloud().getEndPoint());
        if(file!=null ){
            InputStream content = file.getInputStream();//获得指定文件的输入流
            ObjectMetadata meta = new ObjectMetadata();// 创建上传Object的Metadata
            meta.setContentLength(file.getSize());  // 必须设置ContentLength
            obsClient.putObject(storageVo.getHwCloud().getBucketName(),saveFileName,content,meta);// 上传Object.
        }

    }
}
