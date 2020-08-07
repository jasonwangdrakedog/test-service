package com.teg.analysis.util;

import com.teg.analysis.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyuan
 * @date 2020/8/7 15:29
 */
public class DateFormatterUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateFormatterUtil.class);

    private static ThreadLocal<SimpleDateFormat> outDateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM/dd/yyyy"));

    private static ThreadLocal<SimpleDateFormat> inDateFormatHolder = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static Date formatDate(String date) {
        try {
            return inDateFormatHolder.get().parse(date);
        } catch (ParseException e) {
            LOGGER.error("时间格式转换异常", e);
            throw new BusinessException("时间格式异常");
        }
    }
}