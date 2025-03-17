package net.jjjerp.common.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.settings.Express;
import net.jjjerp.common.mapper.settings.ExpressMapper;
import net.jjjerp.common.param.setting.ExpressPageParam;
import net.jjjerp.common.param.setting.ExpressParam;
import net.jjjerp.common.service.bill.BillDeliveryService;
import net.jjjerp.common.service.bill.BillHeadService;
import net.jjjerp.common.service.settings.ExpressService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物流公司记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-19
 */
@Slf4j
@Service
public class ExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements ExpressService {


}
