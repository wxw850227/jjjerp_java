package net.jjjerp.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.file.ImageBank;
import net.jjjerp.common.param.ImageBankPageParam;
import net.jjjerp.common.service.file.ImageBankService;
import net.jjjerp.common.vo.image.ImageBankVo;
import net.jjjerp.config.constant.CommonConstant;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImageBankUtils {

    @Autowired
    private ImageBankService imageBankService;

    //获取系统图库列表
    public List<ImageBankVo> index() {
        List<ImageBank> list = imageBankService.list(new LambdaQueryWrapper<ImageBank>()
                .comment(CommonConstant.NOT_WITH_App_Id)
                .eq(ImageBank::getParentId,0)
                .orderByAsc(ImageBank::getSort).orderByAsc(ImageBank::getCreateTime));
        // 转成vo
        List<ImageBankVo> voList = list.stream().map(e -> {
            ImageBankVo vo = new ImageBankVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

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

    //分页列表
    public Paging<ImageBankVo> list(ImageBankPageParam param) {
        Page<ImageBank> page = new PageInfo<>(param);
        LambdaQueryWrapper<ImageBank> wrapper = new LambdaQueryWrapper<>();
        wrapper.comment(CommonConstant.NOT_WITH_App_Id);
        if(param.getParentId() != null && param.getParentId() != 0){
            wrapper.eq(ImageBank::getParentId, param.getParentId());
        }else {
            //所有图片，不包括一级分类
            wrapper.ne(ImageBank::getParentId, 0);
        }
        wrapper.orderByDesc(ImageBank::getCreateTime);
        IPage<ImageBank> iPage = imageBankService.page(page, wrapper);
        // 最终返回分页对象
        IPage<ImageBankVo> resultPage = iPage.convert(result -> {
            ImageBankVo vo = new ImageBankVo();
            BeanUtil.copyProperties(result, vo);
            return vo;
        });
        return new Paging(resultPage);
    }
}
