package net.jjjerp.common.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import net.jjjerp.common.entity.product.*;
import net.jjjerp.common.mapper.product.ProductMapper;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.param.product.ProductParam;
import net.jjjerp.common.service.product.*;
import net.jjjerp.common.util.ProductUtils;
import net.jjjerp.common.vo.product.ProductImageVo;
import net.jjjerp.common.vo.product.ProductSkuVo;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.exception.BusinessException;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品表 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {


}
