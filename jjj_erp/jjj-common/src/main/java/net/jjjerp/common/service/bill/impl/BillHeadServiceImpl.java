package net.jjjerp.common.service.bill.impl;


import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.mapper.bill.BillHeadMapper;
import net.jjjerp.common.service.bill.BillHeadService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 单据主表 服务实现类
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Slf4j
@Service
public class BillHeadServiceImpl extends BaseServiceImpl<BillHeadMapper, BillHead> implements BillHeadService {

}
