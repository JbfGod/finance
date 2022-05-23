package org.finance.business.convert;

import org.finance.business.entity.ExpenseItem;
import org.finance.business.web.request.AddExpenseBillRequest;
import org.finance.business.web.vo.ExpenseItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */

@Mapper
public interface ExpenseItemConvert {

    ExpenseItemConvert INSTANCE = Mappers.getMapper( ExpenseItemConvert.class );

    ExpenseItemVO toExpenseItemVO(ExpenseItem expenseItem);


}
