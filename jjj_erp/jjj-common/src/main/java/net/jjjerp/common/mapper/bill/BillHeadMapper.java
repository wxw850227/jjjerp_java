package net.jjjerp.common.mapper.bill;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.bill.BillHead;

import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.vo.statement.DealVo;
import net.jjjerp.common.vo.statement.ProductBoardVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 单据主表 Mapper 接口
 *
 * @author jjjerp
 * @since 2024-05-07
 */
@Repository
public interface BillHeadMapper extends BaseMapper<BillHead> {
}
