package org.finance.infrastructure.config.mvc.convert;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
public class StringToLocalDate implements Converter<String, LocalDate> {

    private final static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return LocalDate.parse(source, yyyyMMdd);
    }
}
