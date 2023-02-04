package org.finance.business.entity;

import lombok.Data;
import org.finance.business.entity.enums.Symbol;

/**
 * @author jiangbangfa
 */
@Data
public abstract class AbstractFormula {

    protected Symbol symbol;

}
