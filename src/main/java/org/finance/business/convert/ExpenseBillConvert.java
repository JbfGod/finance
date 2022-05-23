package org.finance.business.convert;

import org.finance.business.entity.ExpenseBill;
import org.finance.business.entity.ExpenseItem;
import org.finance.business.entity.ExpenseItemAttachment;
import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.web.request.AddExpenseBillRequest;
import org.finance.business.web.request.UpdateExpenseBillRequest;
import org.finance.business.web.vo.ExpenseBillDetailVO;
import org.finance.business.web.vo.ExpenseBillPreviewVO;
import org.finance.business.web.vo.ExpenseBillVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author jiangbangfa
 */

@Mapper
public interface ExpenseBillConvert {

    ExpenseBillConvert INSTANCE = Mappers.getMapper( ExpenseBillConvert.class );

    ExpenseBillVO toExpenseBillVO(ExpenseBill expenseBill);

    ExpenseBillDetailVO toExpenseBillDetailVO(ExpenseBill expenseBill);

    ExpenseBillPreviewVO toExpenseBillPreviewVO(ExpenseBill expenseBill);
    ExpenseBill toExpenseBill(AddExpenseBillRequest request);

    ExpenseBill toExpenseBill(UpdateExpenseBillRequest request);

}
