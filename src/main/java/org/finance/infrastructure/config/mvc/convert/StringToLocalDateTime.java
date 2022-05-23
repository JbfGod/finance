package org.finance.infrastructure.config.mvc.convert;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
public class StringToLocalDateTime implements Converter<String, LocalDateTime> {

    private final static DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter yyyyMMddHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

    private final static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDateTime convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        switch (source.length()) {
            case 19:
                return LocalDateTime.parse(source, yyyyMMddHHmmss);
            case 13:
                return LocalDateTime.parse(source, yyyyMMddHH);
            case 10:
                return LocalDate.parse(source, yyyyMMdd).atTime(0, 0);
        }
        return null;
    }
}
