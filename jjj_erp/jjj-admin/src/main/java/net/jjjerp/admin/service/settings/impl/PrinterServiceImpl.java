package net.jjjerp.admin.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.Printer;
import net.jjjerp.common.mapper.settings.PrinterMapper;
import net.jjjerp.admin.service.settings.PrinterService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 小票打印机记录表 服务实现类
 *
 * @author jjjerp
 * @since 2022-07-20
 */
@Slf4j
@Service
public class PrinterServiceImpl extends BaseServiceImpl<PrinterMapper, Printer> implements PrinterService {

}
