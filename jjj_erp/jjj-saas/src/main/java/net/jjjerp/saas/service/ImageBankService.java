package net.jjjerp.saas.service;

import net.jjjerp.saas.param.ImageBankParam;
import net.jjjerp.common.entity.file.ImageBank;
import net.jjjerp.framework.common.service.BaseService;

/**
 * 系统图库表 服务类
 *
 * @author jjjerp
 * @since 2023-10-31
 */
public interface ImageBankService extends BaseService<ImageBank> {

    boolean add(ImageBankParam categoryParam);

    boolean edit(ImageBankParam categoryParam);

    boolean delById(Integer categoryId);

    //批量删除
    boolean deleteFiles(String imageIds);

    //移动图库
    boolean moveFiles(Integer categoryId, String imageIds);

    //新增一级分类
    boolean addCategory(ImageBankParam categoryParam);
}
