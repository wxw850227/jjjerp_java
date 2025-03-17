package net.jjjerp.admin.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.settings.ReturnAddress;
import net.jjjerp.common.mapper.settings.ReturnAddressMapper;
import net.jjjerp.admin.service.settings.ReturnAddressService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 退货地址记录表 服务实现类
 * @author jjjerp
 * @since 2022-07-20
 */
@Slf4j
@Service
public class ReturnAddressServiceImpl extends BaseServiceImpl<ReturnAddressMapper, ReturnAddress> implements ReturnAddressService {

}
