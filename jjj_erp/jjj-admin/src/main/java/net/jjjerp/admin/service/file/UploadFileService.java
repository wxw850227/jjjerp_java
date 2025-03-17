package net.jjjerp.admin.service.file;

import net.jjjerp.common.entity.file.UploadFile;
import net.jjjerp.framework.common.service.BaseService;
import net.jjjerp.framework.core.pagination.Paging;
import net.jjjerp.admin.param.file.UploadFilePageParam;
import net.jjjerp.admin.vo.file.UploadFileVo;

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

    /**
     * 文件列表
     * @param uploadFilePageParam
     * @return
     */
    Paging<UploadFileVo> getList(UploadFilePageParam uploadFilePageParam);
}
