package net.jjjerp.admin.service.file;

import net.jjjerp.common.entity.file.UploadGroup;
import net.jjjerp.framework.common.service.BaseService;

import java.util.List;

/**
 * 文件库分组记录表 服务类
 * @author jjjerp
 * @since 2022-06-28
 */
public interface UploadGroupService extends BaseService<UploadGroup> {

    /**
     * 所有文件分组
     * @return
     */
    List<UploadGroup> getAll(String groupType);

    /**
     * 新增分组
     * @param groupName
     * @param groupType
     * @return
     */
    boolean addGroup(String groupName, String groupType);

    /**
     * 修改分组
     * @param groupId
     * @param groupName
     * @return
     */
    boolean editGroup(Integer groupId, String groupName);

    /**
     * 删除分组
     * @param groupId
     * @return
     */
    boolean deleteGroup(Integer groupId);

    /**
     * 删除文件
     * @param fileIds
     * @return
     */
    boolean deleteFiles(String fileIds);

    /**
     * 移动文件
     * @param fileIds
     * @return
     */
    boolean moveFiles(Integer groupId, String fileIds);
}
