package net.jjjerp.common.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.jjjerp.common.entity.depot.DeliverArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.entity.settings.Region;
import net.jjjerp.common.mapper.depot.DeliverAreaMapper;
import net.jjjerp.common.service.depot.DeliverAreaService;
import net.jjjerp.common.service.settings.RegionService;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 配送区域表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-29
 */
@Slf4j
@Service
public class DeliverAreaServiceImpl extends BaseServiceImpl<DeliverAreaMapper, DeliverArea> implements DeliverAreaService {


}
