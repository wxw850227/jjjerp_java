package net.jjjerp.common.service.file.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.file.ImageBank;
import net.jjjerp.common.mapper.file.ImageBankMapper;
import net.jjjerp.common.service.file.ImageBankService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统图库表 服务实现类
 *
 * @author jjjerp
 * @since 2023-10-31
 */
@Slf4j
@Service
public class ImageBankServiceImpl extends BaseServiceImpl<ImageBankMapper, ImageBank> implements ImageBankService {

    @Autowired
    private ImageBankMapper imageBankMapper;

}
