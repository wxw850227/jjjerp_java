

package net.jjjerp.framework.util;

import net.jjjerp.config.constant.DatePattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getDateString(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.YYYY_MM_DD);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getDateTimeString(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.YYYY_MM_DD_HH_MM_SS);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getDateStart(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getDateEnd(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getDateBetweenString(Date yest,Date now){
        if (now == null || yest == null){
            return null;
        }
        String dateString = "";
        long diff = now.getTime() - yest.getTime();//这样得到的差值是微秒级别
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        long sec = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000* 60)) /1000;
        if(days > 0){
            dateString = dateString + days+"天";
        }
        if(hours > 0){
            dateString = dateString + hours+"小时";
        }
        if(minutes > 0){
            dateString = dateString + minutes+"分";
        }
        if(sec > 0){
            dateString = dateString + sec+"秒";
        }
        return dateString;
    }

    public static String getMd(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getHi(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getTimeString(Date date){
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.HH_MM);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    //是否本年,本月,本天
    public static boolean isThisTime(Date time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(time);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(Date time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(time);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

}
