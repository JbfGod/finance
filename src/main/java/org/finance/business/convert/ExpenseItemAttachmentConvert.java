package org.finance.business.convert;

import org.finance.business.entity.ExpenseItemAttachment;
import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.web.request.AddExpenseBillRequest;
import org.finance.business.web.vo.ExpenseItemSubsidyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */

@Mapper
public interface ExpenseItemAttachmentConvert {

    ExpenseItemAttachmentConvert INSTANCE = Mappers.getMapper( ExpenseItemAttachmentConvert.class );

    ExpenseItemAttachment toExpenseItemAttachment(AddExpenseBillRequest.ItemAttachment itemAttachment);
}
