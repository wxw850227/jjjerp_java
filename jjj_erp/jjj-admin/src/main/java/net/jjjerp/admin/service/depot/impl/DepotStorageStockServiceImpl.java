package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.mapper.depot.DepotStorageStockMapper;
import net.jjjerp.admin.service.depot.DepotStorageService;
import net.jjjerp.admin.service.depot.DepotStorageStockService;
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

    @Autowired
    private DepotStorageStockMapper depotStorageStockMapper;
    @Autowired
    private DepotStorageService depotStorageService;

    //检查商品规格是否已绑定
    @Override
    public Integer checkIsExtendExist(Long productExtendId, Integer storageStockId) {
        LambdaQueryWrapper<DepotStorageStock> wrapper = new LambdaQueryWrapper<>();
        if(storageStockId != null){
            wrapper.ne(DepotStorageStock::getStorageStockId, storageStockId);
        }
        wrapper.eq(DepotStorageStock::getProductExtendId, productExtendId);
        return this.count(wrapper);
    }

    //获取商品绑定货位
    @Override
    public DepotStorage getStorage(Long productExtendId) {
        LambdaQueryWrapper<DepotStorageStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DepotStorageStock::getProductExtendId, productExtendId);
        List<DepotStorageStock> list = this.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            DepotStorageStock stock = list.get(0);
            DepotStorage storage = depotStorageService.getById(stock.getStorageId());
            if(storage != null){
                storage.setStock(stock);
                return storage;
            }
        }
        return null;
    }
}
