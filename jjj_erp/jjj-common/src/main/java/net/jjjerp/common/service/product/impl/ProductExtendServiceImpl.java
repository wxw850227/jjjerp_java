package net.jjjerp.common.service.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.jjjerp.common.entity.bill.BillHead;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.product.*;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.mapper.product.ProductExtendMapper;
import net.jjjerp.common.param.CommonPageParam;
import net.jjjerp.common.param.product.ProductPageParam;
import net.jjjerp.common.service.bill.BillHeadService;
import net.jjjerp.common.service.bill.BillItemService;
import net.jjjerp.common.service.depot.DepotService;
import net.jjjerp.common.service.depot.DepotStorageService;
import net.jjjerp.common.service.depot.DepotStorageStockService;
import net.jjjerp.common.service.product.ProductCategoryService;
import net.jjjerp.common.service.product.ProductExtendService;
import net.jjjerp.common.service.product.ProductMfrsService;
import net.jjjerp.common.service.product.ProductUnitService;
import net.jjjerp.common.service.supplier.SupplierService;
import net.jjjerp.common.util.ProductUtils;
import net.jjjerp.common.vo.product.ProductImageVo;
import net.jjjerp.common.vo.product.ProductListVo;
import net.jjjerp.common.vo.product.ProductSkuVo;
import net.jjjerp.common.vo.product.ProductVo;
import net.jjjerp.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.framework.core.pagination.PageInfo;
import net.jjjerp.framework.core.pagination.Paging;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品价格扩展 服务实现类
 *
 * @author jjjerp
 * @since 2024-04-12
 */
@Slf4j
@Service
public class ProductExtendServiceImpl extends BaseServiceImpl<ProductExtendMapper, ProductExtend> implements ProductExtendService {

}
