package org.finance.business.entity.templates.accountting.system;

import lombok.Data;
import org.finance.business.entity.Report;

import java.math.BigDecimal;

/**
 * 资产负债表
 *
 * @author jiangbangfa
 */
@Data
public class Profit {

    private String name;
    private Integer reportId;
    private Integer rowNum;
    private BigDecimal annualAmount;
    private BigDecimal currentPeriodAmount;


    public Profit() {
    }

    public Profit(Report report) {
        this.name = report.getName();
        this.reportId = report.getId();
        this.rowNum = report.getRowNum();
        this.annualAmount = report.getAnnualAmount();
        this.currentPeriodAmount = report.getCurrentPeriodAmount();
    }

}
