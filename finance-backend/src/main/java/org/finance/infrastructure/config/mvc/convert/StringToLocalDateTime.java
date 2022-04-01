package org.finance.infrastructure.config.mvc.convert;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
public class StringToLocalDateTime implements Converter<String, LocalDateTime> {

    private final static DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter yyyyMMddHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

    @Override
    public LocalDateTime convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try {
            return LocalDateTime.parse(source, yyyyMMddHH);
        } catch (Exception e) {
            return LocalDateTime.parse(source, yyyyMMddHHmmss);
        }
    }
}
