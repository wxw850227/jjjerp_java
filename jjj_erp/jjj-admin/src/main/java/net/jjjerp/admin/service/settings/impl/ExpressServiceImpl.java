package net.jjjerp.admin.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillDelivery;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.settings.Express;
import net.jjjerp.common.mapper.settings.ExpressMapper;
import net.jjjerp.common.param.setting.ExpressPageParam;
import net.jjjerp.common.param.setting.ExpressParam;
import net.jjjerp.admin.service.bill.BillDeliveryService;
import net.jjjerp.admin.service.bill.BillHeadService;
import net.jjjerp.admin.service.settings.ExpressService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物流公司记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-19
 */
@Slf4j
@Service
public class ExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements ExpressService {

    @Resource
    private BillHeadService billHeadService;
    @Autowired
    private BillDeliveryService billDeliveryService;


    /**
     * 物流公司分页查询
     * @param expressPageParam
     * @return
     */
    public Paging<Express> getList(ExpressPageParam expressPageParam) {
        return new Paging<>(this.page(new PageInfo<>(expressPageParam), new LambdaQueryWrapper<Express>().orderByAsc(Express::getSort)));
    }

    /**
     * 获取所有物流公司
     * @param
     * @return
     */
    public List<Express> getAll(){
        return this.list(new LambdaQueryWrapper<Express>().orderByAsc(Express::getSort));
    }

    /**
     * 修改物流公司
     * @param expressParam
     * @return
     */
    public boolean edit(ExpressParam expressParam) {
        Express express = new Express();
        BeanUtils.copyProperties(expressParam, express);
        express.setExpressCode(express.getExpressCode().trim());
        express.setExpressName(express.getExpressName().trim());
        express.setWxCode(express.getWxCode().trim());
        return this.updateById(express);
    }

    /**
     * 添加物流公司
     * @param expressParam
     * @return
     */
    public boolean add(ExpressParam expressParam) {
        Express express = new Express();
        BeanUtils.copyProperties(expressParam, express);
        express.setExpressCode(express.getExpressCode().trim());
        express.setExpressName(express.getExpressName().trim());
        express.setWxCode(express.getWxCode().trim());
        return this.save(express);
    }

    /**
     * 真删除物流公司
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        int count = billDeliveryService.count(new LambdaQueryWrapper<BillDelivery>().eq(BillDelivery::getExpressId, id));
        if (count > 0) {
            throw new BusinessException("存在依赖数据，不允许删除");
        }
        return this.removeById(id);
    }

}
