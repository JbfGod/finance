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
public class AssetsLiability {

    private Integer assetFormulaType;
    private String assetName;
    private Integer assetReportId;
    private Integer assetRowNum;
    private BigDecimal assetEndBalance;
    private BigDecimal assetBeginBalance;
    private Integer assetLevel;

    private Integer liabilityFormulaType;
    private String liabilityName;
    private Integer liabilityReportId;
    private Integer liabilityRowNum;
    private BigDecimal liabilityEndBalance;
    private BigDecimal liabilityBeginBalance;
    private Integer liabilityLevel;


    public AssetsLiability() {
    }

    public AssetsLiability(Report assets, Report liability) {
        if (assets != null) {
            this.assetFormulaType = assets.getFormulaType();
            this.assetName = assets.getName();
            this.assetReportId = assets.getId();
            this.assetRowNum = assets.getRowNum();
            this.assetEndBalance = assets.getEndBalance();
            this.assetBeginBalance = assets.getBeginBalance();
            this.assetLevel = assets.getLevel();
        }
        if (liability != null) {
            this.liabilityFormulaType = liability.getFormulaType();
            this.liabilityName = liability.getName();
            this.liabilityReportId = liability.getId();
            this.liabilityRowNum = liability.getRowNum();
            this.liabilityEndBalance = liability.getEndBalance();
            this.liabilityBeginBalance = liability.getBeginBalance();
            this.liabilityLevel = liability.getLevel();
        }
    }
}
