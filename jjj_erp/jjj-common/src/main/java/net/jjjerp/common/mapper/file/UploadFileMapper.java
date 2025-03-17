package net.jjjerp.common.mapper.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.file.UploadFile;

import org.springframework.stereotype.Repository;


/**
 * 文件库记录表 Mapper 接口
 *
 * @author jjjerp
 * @since 2022-06-28
 */
@Repository
public interface UploadFileMapper extends BaseMapper<UploadFile> {


}
