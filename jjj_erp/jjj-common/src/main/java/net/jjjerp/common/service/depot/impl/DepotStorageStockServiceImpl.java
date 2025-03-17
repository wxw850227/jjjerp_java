package net.jjjerp.common.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.mapper.depot.DepotStorageStockMapper;
import net.jjjerp.common.service.depot.DepotStorageService;
import net.jjjerp.common.service.depot.DepotStorageStockService;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 仓库货位库存表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotStorageStockServiceImpl extends BaseServiceImpl<DepotStorageStockMapper, DepotStorageStock> implements DepotStorageStockService {

}
