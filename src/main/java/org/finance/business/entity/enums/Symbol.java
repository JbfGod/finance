package org.finance.business.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author jiangbangfa
 */
public enum Symbol {

    ADD("+"),
    MINUS("-")
    ;

    @EnumValue
    private String label;

    Symbol(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
