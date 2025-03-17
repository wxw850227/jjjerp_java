package net.jjjerp.common.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.depot.Depot;
import net.jjjerp.common.entity.depot.DepotArea;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.mapper.depot.DepotAreaMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.service.depot.DepotAreaService;
import net.jjjerp.common.service.depot.DepotReservoirService;
import net.jjjerp.common.service.depot.DepotShelfService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库区域表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotAreaServiceImpl extends BaseServiceImpl<DepotAreaMapper, DepotArea> implements DepotAreaService {


}
