package net.jjjerp.common.util;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;
@Slf4j
@Component
public class CodeUtils {

    /**
     * 生成订单号
     * @return
     */
    public static String geneOrderNo(Integer userId){

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

    public static String geneCode(){

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数

        return date + rannum;// 当前时间 + 随机数
    }

    public static String geneBillCode(String code,Integer num){

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        //订单数量,不足4位补0
        int subStrLength = 4;
        String sNum = num.toString();
        int length = sNum.length();
        if (length < subStrLength) {
            sNum = String.format("%0" + subStrLength + "d", num);
        }

        return code + date + sNum;// 订单类型 + 当前时间 + 当天订单数量
    }
}
