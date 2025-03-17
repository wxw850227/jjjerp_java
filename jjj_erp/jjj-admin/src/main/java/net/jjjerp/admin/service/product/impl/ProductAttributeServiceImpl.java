package net.jjjerp.admin.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.mapper.product.ProductAttributeMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.product.ProductAttributeService;
import net.jjjerp.common.settings.vo.StoreVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品属性表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductAttributeServiceImpl extends BaseServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public Paging<ProductAttribute> getproductAttribute(CommonPageParam commonPageParam) {
        Page<ProductAttribute> page = new PageInfo(commonPageParam);
        LambdaQueryWrapper<ProductAttribute> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除"
        wrapper.eq(ProductAttribute::getDeleteFlag, 0);
        if (StringUtils.isNotEmpty(commonPageParam.getAttributeName())) {
            wrapper.like(ProductAttribute::getAttributeName, commonPageParam.getAttributeName());
        }
        wrapper.orderByDesc(ProductAttribute::getId);
        IPage<ProductAttribute> iPage = this.page(page, wrapper);
        IPage<ProductAttribute> result = iPage.convert(this::transVo);
        return new Paging(result);
    }

    @Override
    public boolean add(ProductAttribute param) {
        if(CollectionUtils.isEmpty(param.getValueList())){
            throw new BusinessException("属性值不能为空");
        }
        if(checkIsNameExist(null, param.getAttributeName()) > 0){
            throw new BusinessException("规格名称已存在");
        }
        //数组转字符串
        param.setAttributeValue(String.join("|", param.getValueList()));
        return this.save(param);
    }

    @Override
    public ProductAttribute toEdit(Long id) {
        ProductAttribute att = new ProductAttribute();
        if(id != null){
            att = this.getById(id);
            if(att != null){
                att = transVo(att);
            }
        }
        return att;
    }

    @Override
    public boolean edit(ProductAttribute param) {
        if(CollectionUtils.isEmpty(param.getValueList())){
            throw new BusinessException("属性值不能为空");
        }
        if(checkIsNameExist(param.getId(), param.getAttributeName()) > 0){
            throw new BusinessException("规格名称已存在");
        }
        //数组转字符串
        param.setAttributeValue(String.join("|", param.getValueList()));
        return this.updateById(param);
    }

    public int checkIsNameExist(Long id, String name) {
        LambdaQueryWrapper<ProductAttribute> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(ProductAttribute::getId, id);
        }
        wrapper.eq(ProductAttribute::getAttributeName, name);
        return this.count(wrapper);
    }

    @Override
    public boolean setDelete(Long id) {
        ProductAttribute att = new ProductAttribute();
        att.setId(id);
        //删除标记，0未删除，1删除
        att.setDeleteFlag(1);
        return this.updateById(att);
    }

    @Override
    public List<ProductAttribute> getAll() {
        LambdaQueryWrapper<ProductAttribute> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除"
        wrapper.eq(ProductAttribute::getDeleteFlag, 0);
        wrapper.orderByDesc(ProductAttribute::getId);
        List<ProductAttribute> list = this.list(wrapper);
        list = list.stream().map(this::transVo).collect(Collectors.toList());
        return list;
    }

    //批量删除
    public boolean deletes(String attributeIds) {
        if(StringUtils.isEmpty(attributeIds)){
            throw new BusinessException("请选择属性");
        }
        return this.update(new LambdaUpdateWrapper<ProductAttribute>()
                .set(ProductAttribute::getDeleteFlag, 1)
                .in(ProductAttribute::getId, Arrays.stream(attributeIds.split(",")).map(Integer::valueOf).collect(Collectors.toList())));
    }

    public ProductAttribute transVo(ProductAttribute attr){
        if(StringUtils.isNotEmpty(attr.getAttributeValue())){
            //字符串转数组
            attr.setValueList(Arrays.stream(attr.getAttributeValue().split("\\|")).collect(Collectors.toList()));
        }
        return attr;
    }


}
