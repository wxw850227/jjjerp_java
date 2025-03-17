package net.jjjerp.admin.service.product.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import net.jjjerp.common.entity.product.Product;
import net.jjjerp.common.entity.product.ProductCategory;
import net.jjjerp.common.mapper.product.ProductCategoryMapper;
import net.jjjerp.common.param.product.CategoryParam;
import net.jjjerp.admin.service.product.ProductCategoryService;
import net.jjjerp.admin.service.product.ProductService;
import net.jjjerp.common.vo.product.CategoryVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品类型表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductService productService;

    /**分类列表
     * @return
     */
    public List<CategoryVo> getAll(){
        List<ProductCategory> list = this.list(new LambdaQueryWrapper<ProductCategory>()
                //是否显示1显示0隐藏
                .eq(ProductCategory::getStatus, 1)
                .orderByAsc(ProductCategory::getCreateTime));
        List<CategoryVo> voList = this.transTree(list);
        return voList;
    }

    /**
     * 组装成树形
     * @param list
     * @return
     */
    private List<CategoryVo> transTree(List<ProductCategory> list){
        // 转成vo
        List<CategoryVo> voList = list.stream().map(e -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<CategoryVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<CategoryVo> getChildList(ProductCategory bean, List<CategoryVo> voList) {
        List<CategoryVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getCategoryId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }

    /**
     * 新增
     * @param categoryParam
     * @return
     */
    public Boolean add(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        return this.save(category);
    }
    /**
     * 新增
     * @param categoryParam
     * @return
     */
    public Boolean edit(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        return this.updateById(category);
    }

    /**
     * 真删除
     * @param id
     * @return
     */
    public Boolean delById(Integer id){
        // 是否存在子菜单
        if(this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParentId, id)) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        int productCount = productService.count(new LambdaQueryWrapper<Product>().
                eq(Product::getCategoryId, id).eq(Product::getDeleteFlag, 0));
        if(productCount > 0){
            throw new BusinessException("该分类下存在"+productCount+"个商品，不允许删除");
        }
        return this.removeById(id);
    }

    //修改状态
    @Override
    public boolean setState(Integer categoryId, Integer status) {
        return this.update(new LambdaUpdateWrapper<ProductCategory>().eq(ProductCategory::getCategoryId, categoryId)
                .set(ProductCategory::getStatus, status));
    }

    //查询商品分类ID以及所有子分类ID
    @Override
    public List<Integer> getSubCategoryId(Integer categoryId) {
        List<Integer> ids = this.list(new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getParentId, categoryId)
                        //是否显示1显示0隐藏
                        .eq(ProductCategory::getStatus, 1)
                ).stream().map(ProductCategory::getCategoryId).collect(Collectors.toList());
        ids.add(categoryId);
        return ids;
    }

    //查询分类列表
    @Override
    public List<CategoryVo> getList(CategoryParam categoryParam) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        //是否显示1显示0隐藏
        wrapper.eq(ProductCategory::getStatus, 1)
                .orderByAsc(ProductCategory::getCreateTime);
        if(StringUtils.isNotBlank(categoryParam.getName())){
            wrapper.like(ProductCategory::getName, categoryParam.getName());
        }
        List<ProductCategory> list = this.list(wrapper);
        List<CategoryVo> voList = this.transTree(list);
        return voList;
    }

}
