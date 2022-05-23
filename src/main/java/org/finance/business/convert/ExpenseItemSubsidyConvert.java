package org.finance.business.convert;

import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.web.vo.ExpenseItemSubsidyVO;
import org.finance.business.web.vo.ExpenseItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */

@Mapper
public interface ExpenseItemSubsidyConvert {

    ExpenseItemSubsidyConvert INSTANCE = Mappers.getMapper( ExpenseItemSubsidyConvert.class );

    ExpenseItemSubsidyVO toExpenseItemSubsidy(ExpenseItemSubsidy itemSubsidy);
}
