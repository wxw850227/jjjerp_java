package net.jjjerp.common.service.file.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.file.UploadFile;
import net.jjjerp.common.mapper.file.UploadFileMapper;
import net.jjjerp.common.service.file.UploadFileService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文件库记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-06-28
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {

}
