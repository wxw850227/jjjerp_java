package net.jjjerp.common.util;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjerp.common.entity.bill.BillItem;
import net.jjjerp.common.entity.depot.DepotStorage;
import net.jjjerp.common.entity.depot.DepotStorageStock;
import net.jjjerp.common.entity.product.ProductCategory;
import net.jjjerp.common.entity.product.ProductExtend;
import net.jjjerp.common.entity.product.ProductImage;
import net.jjjerp.common.entity.product.ProductMfrs;
import net.jjjerp.common.enums.BillTypeEnum;
import net.jjjerp.common.enums.BusinessTypeEnum;
import net.jjjerp.common.service.bill.BillItemService;
import net.jjjerp.common.service.depot.DepotStorageService;
import net.jjjerp.common.service.depot.DepotStorageStockService;
import net.jjjerp.common.service.product.ProductCategoryService;
import net.jjjerp.common.service.product.ProductExtendService;
import net.jjjerp.common.service.product.ProductImageService;
import net.jjjerp.common.service.product.ProductMfrsService;
import net.jjjerp.common.vo.product.ProductImageVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderUtils {

    @Autowired
    private ProductExtendService productExtendService;
    @Autowired
    private BillItemService billItemService;

    public static String geneOrderNo(Integer userId) {

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数

        //8位用户id
        int subStrLength = 8;
        String sUserId = userId.toString();
        int length = sUserId.length();
        String str;
        if (length >= subStrLength) {
            str = sUserId.substring(length - subStrLength, length);
        } else {
            str = String.format("%0" + subStrLength + "d", userId);
        }

        return date + str + rannum;// 当前时间 + 用户id + 随机数
    }


}
