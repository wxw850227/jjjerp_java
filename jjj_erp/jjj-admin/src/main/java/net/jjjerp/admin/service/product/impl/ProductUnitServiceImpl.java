package net.jjjerp.admin.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.admin.service.product.ProductDeputyUnitService;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.entity.product.ProductDeputyUnit;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.mapper.product.ProductUnitMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.product.ProductService;
import net.jjjerp.admin.service.product.ProductUnitService;
import net.jjjerp.common.util.StringUtil;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 多单位表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductUnitServiceImpl extends BaseServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {

    @Autowired
    private ProductUnitMapper productUnitMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDeputyUnitService productDeputyUnitService;

    @Override
    public boolean batchSetStatus(Integer state, String ids) {
        List<Long> unitIds = StringUtil.strToLongList(ids);
        List<Product> products = productService.list(new LambdaQueryWrapper<Product>()
                .in(Product::getUnitId, unitIds)
                .eq(Product::getDeleteFlag, 0)
        );
        if(CollectionUtils.isNotEmpty(products) && state != null && state == 0){
            throw new BusinessException("检测到存在依赖数据，不能禁用");
        }
        return this.update(new LambdaUpdateWrapper<ProductUnit>()
                .set(ProductUnit::getEnabled, state)
                .in(ProductUnit::getBasicUnitId, unitIds)
        );
    }

    @Override
    public Paging<ProductUnit> index(CommonPageParam commonPageParam) {
        Page<ProductUnit> page = new PageInfo(commonPageParam);
        LambdaQueryWrapper<ProductUnit> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除"
        wrapper.eq(ProductUnit::getDeleteFlag, 0);
        if (StringUtils.isNotEmpty(commonPageParam.getName())) {
            wrapper.like(ProductUnit::getName, commonPageParam.getName());
        }
        if (commonPageParam.getState() != null && commonPageParam.getState() >= 0) {
            //是否启用,0停用,1启用
            wrapper.eq(ProductUnit::getEnabled, commonPageParam.getState());
        }
        wrapper.orderByDesc(ProductUnit::getCreateTime);
        IPage<ProductUnit> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductUnit> resultPage = iPage.convert(this::transVo);
        return new Paging(resultPage);
    }

    //转换Vo
    @Override
    public ProductUnit transVo(ProductUnit productUnit) {
        List<ProductDeputyUnit> deputyList = productDeputyUnitService.list(new LambdaQueryWrapper<ProductDeputyUnit>()
                .eq(ProductDeputyUnit::getBasicUnitId, productUnit.getBasicUnitId())
                //删除标记，0未删除，1删除
                .eq(ProductDeputyUnit::getIsDelete, 0)
        );
        //按deputyNum排序
        if(CollectionUtils.isNotEmpty(deputyList)) {
            deputyList = deputyList.stream().sorted(Comparator.comparingInt(ProductDeputyUnit::getDeputyNum)).collect(Collectors.toList());
            for(ProductDeputyUnit deputy : deputyList){
                String name = deputy.getDeputyUnitName() + "=" + deputy.getRatio().toString() + productUnit.getBasicUnit();
                deputy.setName(name);
            }
        }
        productUnit.setDeputyList(deputyList);
        return productUnit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ProductUnit unit) {
        parseNameByUnit(unit);
        if(checkIsNameExist(unit.getBasicUnitId(), unit.getName()) > 0){
            throw new BusinessException("该单位已存在");
        }
        this.updateById(unit);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(ProductUnit unit) {
        parseNameByUnit(unit);
        if(checkIsNameExist(unit.getBasicUnitId(), unit.getName()) > 0){
            throw new BusinessException("该单位已存在");
        }
        return this.updateById(unit);
    }

    public int checkIsNameExist(Long id, String name) {
        LambdaQueryWrapper<ProductUnit> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(ProductUnit::getBasicUnitId, id);
        }
        wrapper.eq(ProductUnit::getName, name);
        //删除标记，0未删除，1删除
        wrapper.eq(ProductUnit::getDeleteFlag, 0);
        return this.count(wrapper);
    }

    /**
     * 根据单位信息生成名称的格式
     * @param unit
     */
    private void parseNameByUnit(ProductUnit unit) {
        String unitName = unit.getBasicUnit();
        if(unit.getBasicUnitId() == null){
            unit.setEnabled(1);
            this.save(unit);
        }
        List<Integer> deleteList = unit.getDeleteList();
        if (CollectionUtils.isNotEmpty(deleteList)){
            productDeputyUnitService.remove(new LambdaQueryWrapper<ProductDeputyUnit>()
                    .in(ProductDeputyUnit::getDeputyUnitId, deleteList)
            );
        }
        List<ProductDeputyUnit> deputyList = unit.getDeputyList();
        //按deputyNum排序
        if(CollectionUtils.isNotEmpty(deputyList)){
            deputyList = deputyList.stream().filter(o -> StringUtils.isNotBlank(o.getDeputyUnitName()) || o.getRatio() != null).collect(Collectors.toList());
            for(ProductDeputyUnit deputy : deputyList){
                if(deputy.getDeputyNum() == null){
                    throw new BusinessException("副单位层级不能为空");
                }
            }
            deputyList = deputyList.stream().sorted(Comparator.comparingInt(ProductDeputyUnit::getDeputyNum)).collect(Collectors.toList());
            for(ProductDeputyUnit deputy : deputyList){
                deputy.setBasicUnitId(unit.getBasicUnitId());
                if(deputy.getDeputyUnitId() == null){
                    productDeputyUnitService.save(deputy);
                }else {
                    productDeputyUnitService.updateById(deputy);
                }
                if(StringUtils.isBlank(deputy.getDeputyUnitName())){
                    throw new BusinessException("副单位名称不能为空");
                }
                if(deputy.getRatio() == null){
                    throw new BusinessException("比例不能为空");
                }
                unitName += "/" + "(" +  deputy.getDeputyUnitName() + "=" + deputy.getRatio().toString() + unit.getBasicUnit() + ")";
            }
        }
        unit.setName(unitName);
    }

    @Override
    public boolean delById(String ids) {
        List<Long> unitIds = StringUtil.strToLongList(ids);
        List<Product> products = productService.list(new LambdaQueryWrapper<Product>()
                .in(Product::getUnitId, unitIds)
                .eq(Product::getDeleteFlag, 0)
        );
        if(CollectionUtils.isNotEmpty(products)){
            throw new BusinessException("检测到存在依赖数据，不能删除");
        }
        return this.update(new LambdaUpdateWrapper<ProductUnit>()
                //删除标记，0未删除，1删除
                .set(ProductUnit::getDeleteFlag, 1)
                .in(ProductUnit::getBasicUnitId, unitIds)
        );
    }

    @Override
    public List<ProductUnit> getAll() {
        LambdaQueryWrapper<ProductUnit> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除"
        wrapper.eq(ProductUnit::getDeleteFlag, 0);
        wrapper.orderByDesc(ProductUnit::getCreateTime);
        return this.list(wrapper);
    }
}
