package net.jjjerp.admin.mapper.bill;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.vo.statement.BacklogVo;
import net.jjjerp.common.vo.statement.DealVo;
import net.jjjerp.common.vo.statement.HomeDealVo;
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

    //作业看板
    @InterceptorIgnore(tenantLine = "true")
    Map<String, Object> jobBoard(CommonPageParam param);

    //交易趋势
    @InterceptorIgnore(tenantLine = "true")
    DealVo dealBoard(CommonPageParam param);

    //商品排行
    List<ProductBoardVo> productBoard(CommonPageParam param);

    //首页交易趋势
    @InterceptorIgnore(tenantLine = "true")
    HomeDealVo dealHome(CommonPageParam param);

    //今日待办
    @InterceptorIgnore(tenantLine = "true")
    BacklogVo backlog(CommonPageParam param);
}
