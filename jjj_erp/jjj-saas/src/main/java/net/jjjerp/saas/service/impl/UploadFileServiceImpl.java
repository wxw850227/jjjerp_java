package net.jjjerp.saas.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.saas.param.UploadFilePageParam;
import net.jjjerp.saas.service.UploadFileService;
import net.jjjerp.saas.vo.UploadFileVo;
import net.jjjerp.common.entity.file.UploadFile;
import net.jjjerp.common.mapper.file.UploadFileMapper;
import net.jjjerp.common.util.UploadFileUtils;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 文件库记录表 服务实现类
 * @author jjjerp
 * @since 2022-06-28
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {
    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 保存图片
     * @param file
     * @return
     */
    public boolean addFile(UploadFile file){
        return this.save(file);
    }

    /**
     * 文件列表
     * @param uploadFilePageParam
     * @return
     */
    public Paging<UploadFileVo> getList(UploadFilePageParam uploadFilePageParam){
        Page<UploadFile> page = new PageInfo<>(uploadFilePageParam);
        LambdaQueryWrapper<UploadFile> wrapper = new LambdaQueryWrapper<UploadFile>();
        wrapper.eq(UploadFile::getIsDelete, 0);
        if(uploadFilePageParam.getGroupId() != null && uploadFilePageParam.getGroupId().intValue() != 0){
            wrapper.eq(UploadFile::getGroupId, uploadFilePageParam.getGroupId());
        }
        if(!StringUtils.isEmpty(uploadFilePageParam.getFileType())){
            wrapper.eq(UploadFile::getFileType, uploadFilePageParam.getFileType());
        }
        wrapper.orderByDesc(UploadFile::getCreateTime);
        IPage<UploadFile> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<UploadFileVo> resultPage = iPage.convert(result -> {
            UploadFileVo vo = new UploadFileVo();
            BeanUtil.copyProperties(result, vo);
            vo.setFilePath(uploadFileUtils.getFilePathByFile(vo));
            return vo;
        });
        return new Paging(resultPage);
    }
}
