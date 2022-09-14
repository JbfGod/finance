package org.finance.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.ApprovalInstanceApprover;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 审批流相关的审核人员 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
public interface ApprovalInstanceApproverMapper extends BaseMapper<ApprovalInstanceApprover> {

    List<Long> customerIdsByApproverId(@Param("approverId") long approverId);

}
