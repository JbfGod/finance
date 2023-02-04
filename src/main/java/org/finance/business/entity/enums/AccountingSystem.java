package org.finance.business.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.finance.business.entity.templates.accountting.system.AbstractSystem;
import org.finance.business.entity.templates.accountting.system.N1;
import org.finance.business.entity.templates.accountting.system.N2;
import org.finance.business.entity.templates.accountting.system.N3;
import org.finance.business.entity.templates.accountting.system.N4;

/**
 * 会计制度
 * @author jiangbangfa
 */
public enum AccountingSystem {

    N1(1, "小企业会计准则（2013年）"),
    N2(2, "农民专业合作社财务会计制度"),
    N3(3, "村集体经济组织会计制度"),
    N4(4, "新会计准则（2019年）"),
    /*N4(5, ""),
    N5(6, "")*/
    ;

    @EnumValue
    private int value;
    private String label;

    AccountingSystem(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public AbstractSystem getSystem() {
        switch (this.value) {
            case 1:
                return new N1();
            case 2:
                return new N2();
            case 3:
                return new N3();
            case 4:
                return new N4();
        }
        return new N1();
    }

}
