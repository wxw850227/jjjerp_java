package net.jjjerp.admin.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.entity.product.ProductUnit;
import net.jjjerp.common.mapper.product.ProductMfrsMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.product.ProductMfrsService;
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

import java.util.List;

/**
 * 产品品牌表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-15
 */
@Slf4j
@Service
public class ProductMfrsServiceImpl extends BaseServiceImpl<ProductMfrsMapper, ProductMfrs> implements ProductMfrsService {

    @Autowired
    private ProductMfrsMapper productMfrsMapper;

    @Override
    public Paging<ProductMfrs> index(CommonPageParam commonPageParam) {
        Page<ProductMfrs> page = new PageInfo(commonPageParam);
        LambdaQueryWrapper<ProductMfrs> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(commonPageParam.getSearch())) {
            wrapper.like(ProductMfrs::getName, commonPageParam.getSearch());
        }
        wrapper.orderByDesc(ProductMfrs::getCreateTime);
        IPage<ProductMfrs> iPage = this.page(page, wrapper);
        return new Paging(iPage);
    }

    @Override
    public boolean add(ProductMfrs mfrs) {
        if(checkIsNameExist(null, mfrs.getName()) > 0){
            throw new BusinessException("该品牌已存在");
        }
        return this.save(mfrs);
    }

    @Override
    public boolean edit(ProductMfrs mfrs) {
        if(checkIsNameExist(mfrs.getId(), mfrs.getName()) > 0){
            throw new BusinessException("该品牌已存在");
        }
        return this.updateById(mfrs);
    }

    public int checkIsNameExist(Integer id, String name) {
        LambdaQueryWrapper<ProductMfrs> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(ProductMfrs::getId, id);
        }
        wrapper.eq(ProductMfrs::getName, name);
        return this.count(wrapper);
    }

    @Override
    public boolean delById(Integer id) {
        return this.removeById(id);
    }

    @Override
    public List<ProductMfrs> getAll() {
        LambdaQueryWrapper<ProductMfrs> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ProductMfrs::getCreateTime);
        return this.list(wrapper);
    }
}
