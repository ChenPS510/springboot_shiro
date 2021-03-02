package com.mk.shiro.config;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * @Author: mk.chen
 * @Date: 2020/1/16 15:14
 * @Description:
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {
    private static final String DATE_SPLIT = "-";
    private static final String TIME_SPLIT = ":";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_STAMP_PATTERN = "^\\d+$";
    private static final Logger LOG = LoggerFactory.getLogger(StringToDateConverter.class);

    public StringToDateConverter() {
    }

    @Override
    public Date convert(@Nullable String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        } else {
            source = source.trim();

            try {
                if (source.contains("-")) {
                    FastDateFormat formatter;
                    if (source.contains(":")) {
                        formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
                    } else {
                        formatter = FastDateFormat.getInstance("yyyy-MM-dd");
                    }

                    return formatter.parse(source);
                } else if (source.matches("^\\d+$")) {
                    Long lDate = new Long(source);
                    return new Date(lDate);
                } else {
                    throw new IllegalArgumentException("不支持的格式");
                }
            } catch (Exception var3) {
                LOG.error(String.format("parser %s to Date fail", source), var3.getMessage());
                throw new HttpMessageConversionException("时间参数格式错误");
            }
        }
    }
}

