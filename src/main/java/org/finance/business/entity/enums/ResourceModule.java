package org.finance.business.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author jiangbangfa
 */
public enum ResourceModule {

    MANAGE(1),
    FINANCE(2),
    ;

    @EnumValue
    private int value;
    ResourceModule(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
