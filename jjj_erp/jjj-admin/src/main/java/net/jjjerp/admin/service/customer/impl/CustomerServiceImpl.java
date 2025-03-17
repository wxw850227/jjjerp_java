package net.jjjerp.admin.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.jjjerp.common.cache.RegionCache;
import net.jjjerp.common.entity.customer.Customer;
import net.jjjerp.common.entity.customer.CustomerAddress;
import net.jjjerp.common.entity.product.ProductAttribute;
import net.jjjerp.common.enums.StoreTypeEnum;
import net.jjjerp.common.mapper.customer.CustomerMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.admin.service.customer.CustomerAddressService;
import net.jjjerp.admin.service.customer.CustomerService;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户记录表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-22
 */
@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerAddressService customerAddressService;
    @Autowired
    private RegionCache regionCache;

    @Override
    public Paging<Customer> getList(CommonPageParam param) {
        // 客户列表
        Page<Customer> page = new PageInfo<>(param);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        //删除标记，0未删除，1删除
        wrapper.eq(Customer::getIsDelete, 0);
        //编码搜索
        if(StringUtils.isNotEmpty(param.getCode())){
            wrapper.like(Customer::getUserCode, param.getCode());
        }
        //姓名搜索
        if(StringUtils.isNotEmpty(param.getName())){
            wrapper.like(Customer::getRealName, param.getName());
        }
        //昵称搜索
        if(StringUtils.isNotEmpty(param.getNickName())){
            wrapper.like(Customer::getNickName, param.getNickName());
        }
        //手机号搜索
        if(StringUtils.isNotEmpty(param.getPhone())){
            wrapper.like(Customer::getPhone, param.getPhone());
        }
        //平台类型
        if(param.getType() != null && param.getType() > 0){
            wrapper.eq(Customer::getPlatformType, param.getType());
        }
        wrapper.orderByDesc(Customer::getCreateTime);
        IPage<Customer> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<Customer> result = iPage.convert(this::transVo);
        return new Paging<>(result);
    }

    private Customer transVo(Customer customer) {
        customer.setAddressList(customerAddressService.list(new LambdaQueryWrapper<CustomerAddress>().eq(CustomerAddress::getCustomerId, customer.getCustomerId())));
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Customer customer) {
        if(this.checkIsNameExist(null, customer.getNickName()) > 0){
            throw new BusinessException("昵称已存在");
        }
        if(customer.getInitial() == null){
            throw new BusinessException("期初应付款不能为空");
        }
        //欠款总额
        customer.setDebtCredit(customer.getInitial());
        //当前应付 = 期初应付
        customer.setAllNeedPay(customer.getInitial());
        this.save(customer);
        if(CollectionUtils.isNotEmpty(customer.getAddressList())){
            //保存客户地址
            customer.getAddressList().forEach(address -> {
                address.setCustomerId(customer.getCustomerId());
                customerAddressService.save(address);
            });
        }
        return true;
    }

    public int checkIsNameExist(Integer id, String nickName) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        if(id != null){
            wrapper.ne(Customer::getCustomerId, id);
        }
        wrapper.eq(Customer::getNickName, nickName);
        //删除标记，0未删除，1删除
        wrapper.eq(Customer::getIsDelete, 0);
        return this.count(wrapper);
    }

    @Override
    public Map<String, Object> toEdit(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("region",regionCache.getCacheTree());
        map.put("platformType", StoreTypeEnum.getList());
        Customer customer = this.getById(userId);
        if(customer != null){
            customer.setAddressList(customerAddressService.list(new LambdaQueryWrapper<CustomerAddress>().eq(CustomerAddress::getCustomerId, userId)));
        }
        map.put("model", customer);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(Customer customer) {
        if(StringUtils.isEmpty(customer.getNickName())){
            throw new BusinessException("昵称不能为空");
        }else {
            if(this.checkIsNameExist(customer.getCustomerId(), customer.getNickName()) > 0){
                throw new BusinessException("昵称已存在");
            }
        }
        //金额不能编辑
        //当前应付
        customer.setAllNeedPay(null);
        //已付预付款
        customer.setDeposit(null);
        //期初应收款
        customer.setInitial(null);
        //欠款总额
        customer.setDebtCredit(null);
        this.updateById(customer);
        //保存客户地址
        if(CollectionUtils.isEmpty(customer.getAddressList())){
            customerAddressService.remove(new LambdaQueryWrapper<CustomerAddress>().eq(CustomerAddress::getCustomerId, customer.getCustomerId()));
        }else {
            customer.getAddressList().forEach(address -> {
                address.setCustomerId(customer.getCustomerId());
                if(address.getAddressId() != null){
                    customerAddressService.updateById(address);
                }else {
                    customerAddressService.save(address);
                }
            });
        }
        return true;
    }

    @Override
    public boolean delById(Integer userId) {
        Customer customer = this.getById(userId);
        //删除标记，0未删除，1删除
        customer.setIsDelete(1);
        return this.updateById(customer);
    }
}
