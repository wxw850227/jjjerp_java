package net.jjjerp.saas.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.ImageBankParam;
import net.jjjerp.saas.service.ImageBankService;
import net.jjjerp.common.entity.file.ImageBank;
import net.jjjerp.common.mapper.file.ImageBankMapper;
import net.jjjerp.common.vo.image.ImageBankVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统图库表 服务实现类
 *
 * @author jjjerp
 * @since 2023-10-31
 */
@Slf4j
@Service
public class ImageBankServiceImpl extends BaseServiceImpl<ImageBankMapper, ImageBank> implements ImageBankService {

    @Autowired
    private ImageBankMapper imageBankMapper;


    /**
     * 组装成树形
     * @param list
     * @return
     */
    private List<ImageBankVo> transTree(List<ImageBank> list){
        // 转成vo
        List<ImageBankVo> voList = list.stream().map(e -> {
            ImageBankVo vo = new ImageBankVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        // 遍历成树形结构
        List<ImageBankVo> collect = voList.stream()
                // 2. 找出所有顶级（规定 0 为顶级）
                .filter(o -> StrUtil.equals("0", String.valueOf(o.getParentId())))
                // 3.给当前父级的 childList 设置子
                .peek(o -> o.setChildren(getChildList(o, voList)))
                // 4.收集
                .collect(Collectors.toList());
        return collect;
    }

    // 根据当前父类 找出子类， 并通过递归找出子类的子类
    private List<ImageBankVo> getChildList(ImageBank bean, List<ImageBankVo> voList) {
        List<ImageBankVo> list = voList.stream()
                //筛选出父节点id == parentId 的所有对象 => list
                .filter(o -> StrUtil.equals(String.valueOf(bean.getCategoryId()), String.valueOf(o.getParentId())))
                .peek(o -> o.setChildren(getChildList(o, voList)))
                .collect(Collectors.toList());
        if(list.size() == 0){
            return null;
        }
        return list;
    }

    @Override
    public boolean add(ImageBankParam param) {
        if(param.getParentId() == null || param.getParentId() == 0){
            throw new BusinessException("请先选择图片分类");
        }
        if(StringUtils.isEmpty(param.getImage())){
            throw new BusinessException("图片路径不能为空");
        }
        ImageBank bean = new ImageBank();
        BeanUtils.copyProperties(param, bean);
        return this.save(bean);
    }

    @Override
    public boolean edit(ImageBankParam param) {
        ImageBank bean = new ImageBank();
        BeanUtils.copyProperties(param, bean);
        return this.updateById(bean);
    }

    @Override
    public boolean delById(Integer id) {
        // 是否存在子菜单
        if(this.count(new LambdaQueryWrapper<ImageBank>().eq(ImageBank::getParentId, id)) > 0){
            throw new BusinessException("当前分类下存在文件，请先删除");
        }
        return this.removeById(id);
    }

    //批量删除
    @Override
    public boolean deleteFiles(String imageIds) {
        if(StringUtils.isEmpty(imageIds)){
            throw new BusinessException("请选择图片");
        }
        return this.remove(new LambdaUpdateWrapper<ImageBank>()
                        .in(ImageBank::getCategoryId, this.transFileIds(imageIds)));
    }

    /**
     * 移动文件
     * @param imageIds
     * @return
     */
    public boolean moveFiles(Integer categoryId, String imageIds){
        return this.update(new LambdaUpdateWrapper<ImageBank>()
                .in(ImageBank::getCategoryId, this.transFileIds(imageIds))
                .set(ImageBank::getParentId, categoryId));
    }

    //新增一级分类
    @Override
    public boolean addCategory(ImageBankParam param) {
        ImageBank bean = new ImageBank();
        BeanUtils.copyProperties(param, bean);
        bean.setParentId(0);
        return this.save(bean);
    }

    private List<Integer> transFileIds(String imageIds) {
        String[] split = StringUtils.split(imageIds, ",");
        List<Integer> files = new ArrayList<>();
        for (String fileId : split) {
            files.add(Integer.parseInt(fileId));
        }
        return files;
    }
}
