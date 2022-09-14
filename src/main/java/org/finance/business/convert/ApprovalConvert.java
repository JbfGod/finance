package org.finance.business.convert;

import org.finance.business.entity.ApprovalFlowItem;
import org.finance.business.entity.ApprovalInstance;
import org.finance.business.entity.ApprovalInstanceItem;
import org.finance.business.web.vo.ApprovalFlowItemVO;
import org.finance.business.web.vo.ApprovalInstanceItemVO;
import org.finance.business.web.vo.ApprovalInstanceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Mapper
public interface ApprovalConvert {

    ApprovalConvert INSTANCE = Mappers.getMapper( ApprovalConvert.class );

    List<ApprovalInstanceItemVO> toApprovalInstanceItemVO(List<ApprovalInstanceItem> approvalInstanceItems);

    ApprovalInstanceVO toApprovalInstanceVO(ApprovalInstance approvalInstance);

    ApprovalFlowItemVO toApprovalFlowItemVO(ApprovalFlowItem approvalFlowItem);

}
