package net.jjjerp.saas.service;

import net.jjjerp.saas.param.UploadFilePageParam;
import net.jjjerp.saas.vo.UploadFileVo;
import net.jjjerp.common.entity.file.UploadFile;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;

/**
 * 文件库记录表 服务类
 * @author jjjerp
 * @since 2022-06-28
 */
public interface UploadFileService extends BaseService<UploadFile> {

    /**
     * 保存文件
     * @param file
     * @return
     */
    boolean addFile(UploadFile file);

    Paging<UploadFileVo> getList(UploadFilePageParam uploadFilePageParam);
}
