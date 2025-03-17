package net.jjjerp.admin.service.depot.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.depot.DepotReservoir;
import net.jjjerp.common.entity.depot.DepotShelf;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.store.Store;
import net.jjjerp.common.mapper.depot.DepotStorageMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.depot.StorageParam;
import net.jjjerp.admin.service.depot.DepotReservoirService;
import net.jjjerp.admin.service.depot.DepotShelfService;
import net.jjjerp.admin.service.depot.DepotStorageService;
import net.jjjerp.admin.service.depot.DepotStorageStockService;
import net.jjjerp.admin.service.product.ProductExtendService;
import net.jjjerp.admin.service.product.ProductService;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库货位表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class DepotStorageServiceImpl extends BaseServiceImpl<DepotStorageMapper, DepotStorage> implements DepotStorageService {

    @Autowired
    private DepotStorageMapper depotStorageMapper;
    @Autowired
    private DepotStorageStockService depotStorageStockService;
    @Autowired
    private DepotReservoirService reservoirService;
    @Autowired
    private DepotShelfService shelfService;
    @Autowired
    private ProductExtendService productExtendService;
    @Autowired
    private ProductService productService;

    @Override
    public Paging<DepotStorage> getList(CommonPageParam param) {
        // 商品列表
        Page<DepotStorage> page = new PageInfo<>(param);
        LambdaQueryWrapper<DepotStorage> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(DepotStorage::getStatus, 1);
        //编码搜索
        if(StringUtils.isNotEmpty(param.getCode())){
            wrapper.like(DepotStorage::getStorageCode, param.getCode());
        }
        //货位类型,1拣货货位,2存储货位,3异常货位
        if(param.getType() != null && param.getType() > 0){
            wrapper.eq(DepotStorage::getStorageType, param.getType());
        }
        //商品规格id
        if(param.getProductExtendId() != null && param.getProductExtendId() > 0){
            List<Integer> ids = depotStorageStockService.list(new LambdaQueryWrapper<DepotStorageStock>()
                            .eq(DepotStorageStock::getProductExtendId, param.getProductExtendId())
            ).stream().map(DepotStorageStock::getStorageId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(ids)){
                ids.add(-1);
            }
            wrapper.in(DepotStorage::getDepotStorageId, ids);
        }
        //仓库id
        if(param.getDepotId() != null && param.getDepotId() > 0){
            List<Integer> depotReservoirIds = reservoirService.list(new LambdaQueryWrapper<DepotReservoir>()
                    .eq(DepotReservoir::getDepotId, param.getDepotId())
            ).stream().map(DepotReservoir::getDepotReservoirId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(depotReservoirIds)){
                depotReservoirIds.add(-1);
            }
            wrapper.in(DepotStorage::getReservoirId, depotReservoirIds);
        }
        //区域id
        if(param.getAreaId() != null && param.getAreaId() > 0){
            List<Integer> depotReservoirIds = reservoirService.list(new LambdaQueryWrapper<DepotReservoir>()
                    .eq(DepotReservoir::getAreaId, param.getAreaId())
            ).stream().map(DepotReservoir::getDepotReservoirId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(depotReservoirIds)){
                depotReservoirIds.add(-1);
            }
            wrapper.in(DepotStorage::getReservoirId, depotReservoirIds);
        }
        //库区id
        if(param.getReservoirId() != null && param.getReservoirId() > 0){
            wrapper.eq(DepotStorage::getReservoirId, param.getReservoirId());
        }
        //货架id
        if(param.getDepotShelfId() != null && param.getDepotShelfId() > 0){
            wrapper.eq(DepotStorage::getSheifId, param.getDepotShelfId());
        }
        wrapper.orderByDesc(DepotStorage::getCreateTime);
        IPage<DepotStorage> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<DepotStorage> resultPage = iPage.convert(this::transVo);
        return new Paging<>(iPage);
    }

    private DepotStorage transVo(DepotStorage storage) {
        List<DepotStorageStock> list = depotStorageStockService.list(new LambdaQueryWrapper<DepotStorageStock>()
                .eq(DepotStorageStock::getStorageId, storage.getDepotStorageId())
        );
        StringBuilder stockStr = new StringBuilder();
        for(DepotStorageStock stock : list){
            ProductExtend extend = productExtendService.getById(stock.getProductExtendId());
            Long stockNum = stock.getStockNum();
            if(extend != null){
                String productCode = extend.getProductCode();
                stockStr.append("[商品:").append(productCode).append(",");
                stockStr.append("数量:").append(stockNum).append("]");
                //判断如果不是最后一个元素
                if(list.indexOf(stock) != list.size() - 1){
                    stockStr.append(",");
                }
            }
        }
        storage.setStockStr(stockStr.toString());
        if(storage.getReservoirId() != null && storage.getReservoirId() > 0){
            DepotReservoir reservoir = reservoirService.getById(storage.getReservoirId());
            if(reservoir != null){
                storage.setReservoirName(reservoir.getReservoirName());
            }
        }
        return storage;
    }


    @Override
    public boolean add(DepotStorage storage) {
        if(storage.getReservoirId() == null && storage.getSheifId() == null){
            throw new BusinessException("库区和货架至少选择一个");
        }
        DepotReservoir reservoir;
        if(storage.getReservoirId() != null){
            reservoir = reservoirService.getById(storage.getReservoirId());
        }else {
            DepotShelf shelf = shelfService.getById(storage.getSheifId());
            reservoir = reservoirService.getById(shelf.getReservoirId());
            storage.setReservoirId(reservoir.getDepotReservoirId());
        }
        if(reservoir == null){
            throw new BusinessException("库区不存在");
        }
        if(this.checkIsCodeExist(null,reservoir.getDepotId(), storage.getStorageCode()) > 0){
            throw new BusinessException("编码已存在");
        }
        storage.setDepotId(reservoir.getDepotId());
        return this.save(storage);
    }

    public int checkIsCodeExist(Integer id,Long depotId, String code) {
        LambdaQueryWrapper<DepotStorage> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(DepotStorage::getDepotStorageId, id);
        }
        wrapper.eq(DepotStorage::getStorageCode, code);
        wrapper.eq(DepotStorage::getDepotId, depotId);
        //是否显示1显示0隐藏
        wrapper.eq(DepotStorage::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public boolean edit(DepotStorage storage) {
        if(this.checkIsCodeExist(storage.getDepotStorageId(),storage.getDepotId(), storage.getStorageCode()) > 0){
            throw new BusinessException("编码已存在");
        }
        return this.updateById(storage);
    }

    @Override
    public boolean delById(Integer depotStorageId) {
        //货位库存List
        List<DepotStorageStock> stockList = depotStorageStockService.list(new LambdaQueryWrapper<DepotStorageStock>()
                .eq(DepotStorageStock::getStorageId,depotStorageId)
        );
        if(CollectionUtils.isNotEmpty(stockList)){
            throw new BusinessException("货位不是空的, 请先删除货位下的库存后再重试");
        }
        DepotStorage storage= new DepotStorage();
        storage.setDepotStorageId(depotStorageId);
        //是否显示1显示0隐藏
        storage.setStatus(0);
        return this.updateById(storage);
    }

    //设置货位库存
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean settingStock(DepotStorage storage) {
        if(storage.getDepotStorageId() == null){
            throw new BusinessException("货位id不能为空");
        }
        List<DepotStorageStock> stockList = storage.getStockList();
        if(CollectionUtils.isEmpty(stockList)){
            //删除所有库存
            depotStorageStockService.remove(new LambdaQueryWrapper<DepotStorageStock>()
                    .eq(DepotStorageStock::getStorageId,storage.getDepotStorageId())
            );
        }else {
            for(DepotStorageStock stock : stockList){
                if(stock.getProductExtendId() == null){
                    throw new BusinessException("商品规格id不能为空");
                }
                if(stock.getStockNum() == null){
                    throw new BusinessException("库存数量不能为空");
                }
                ProductExtend extend = productExtendService.getById(stock.getProductExtendId());
                //库存数量不能大于该商品规格现有库存
                if(stock.getStockNum() > extend.getStockNum()){
                    throw new BusinessException("库存数量不能大于该商品规格现有库存"+extend.getStockNum());
                }
                stock.setStorageId(storage.getDepotStorageId());
                if(stock.getStorageStockId() != null){
                    //是否删除,1是,0否
                    if(stock.getIsDelete() != null && stock.getIsDelete() == 1){
                        depotStorageStockService.removeById(stock.getStorageStockId());
                    }else {
                        //修改,检查商品规格是否已绑定
                        if(depotStorageStockService.checkIsExtendExist(stock.getProductExtendId(),stock.getStorageStockId()) > 0){
                            throw new BusinessException("商品规格id"+stock.getProductExtendId()+"已绑定,不能重复绑定");
                        }
                        depotStorageStockService.updateById(stock);
                    }
                }else {
                    //新增,检查商品规格是否已绑定
                    if(depotStorageStockService.checkIsExtendExist(stock.getProductExtendId(),stock.getStorageStockId()) > 0){
                        throw new BusinessException("商品规格编码"+extend.getProductCode()+"已绑定,不能重复绑定");
                    }
                    depotStorageStockService.save(stock);
                }
            }
        }
        return true;
    }

    @Override
    public DepotStorage getStock(Integer depotStorageId) {
        DepotStorage storage = this.getById(depotStorageId);
        if(storage != null){
            storage.setStockList(depotStorageStockService.list(new LambdaQueryWrapper<DepotStorageStock>()
                    .eq(DepotStorageStock::getStorageId,depotStorageId)
            ));
            for(DepotStorageStock stock : storage.getStockList()){
                ProductExtend extend = productExtendService.getById(stock.getProductExtendId());
                if(extend != null){
                    stock.setImageUrl(extend.getImageUrl());
                    stock.setProductCode(extend.getProductCode());
                    stock.setSku(extend.getSku());
                    Product product = productService.getById(extend.getProductId());
                    if(product != null){
                        stock.setProductName(product.getName());
                    }
                }
            }
        }
        return storage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> batchAdd(StorageParam param) {
        List<DepotStorage> list = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        //巷道数量
        for (int i = 0; i < param.getAddRow(); i++) {
            Integer row = param.getRow() + i;
            //架数量
            for (int j = 0; j < param.getAddCol(); j++) {
                Integer columns = param.getCell() + j;
                //层数量
                for (int k = 0; k < param.getAddFloor(); k++) {
                    Integer floor = param.getFloor() + k;
                    //格数量
                    for (int l = 0; l < param.getAddCell(); l++) {
                        Integer cell = param.getColumns() + l;
                        String storageCode = batchCreateNo(param.getReservoirName(),row,columns,floor,cell);
                        if(this.checkIsCodeExist(null,param.getDepotId(), storageCode) > 0){
                            storageCode += "(在该仓库已存在,不会新增)";
                            codeList.add(storageCode);
                            continue;
                        }else {
                            codeList.add(storageCode);
                        }
                        DepotStorage storage = new DepotStorage();
                        storage.setDepotId(param.getDepotId());
                        storage.setStorageCode(storageCode);
                        storage.setReservoirId(param.getReservoirId());
                        storage.setSheifId(param.getSheifId());
                        storage.setStorageType(param.getStorageType());
                        list.add(storage);
                   }
               }
           }
        }
        //类型,0预览,1添加
        if(param.getType() == 1 && CollectionUtils.isNotEmpty(list)){
            this.saveBatch(list);
        }
        return codeList;
    }

    //生成货位编码
    private String batchCreateNo(String reservoirName, Integer row, Integer columns, Integer floor, Integer cell) {
        //生成货位编码,以库区名称+巷道+列+层+格,中间用-拼接,不足两位数时补0
        StringBuilder sb = new StringBuilder();
        sb.append(reservoirName);
        if(row < 10){
            sb.append("-0").append(row);
        }else {
            sb.append("-").append(row);
        }
        if(columns < 10){
            sb.append("-0").append(columns);
        }else {
            sb.append("-").append(columns);
        }
        if(floor < 10){
            sb.append("-0").append(floor);
        }else {
            sb.append("-").append(floor);
        }
        if(cell < 10){
            sb.append("-00").append(cell);
        }else  if(cell < 100){
            sb.append("-0").append(cell);
        }else {
            sb.append("-").append(cell);
        }
        return sb.toString();
    }
}
