package org.finance.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.ApprovalInstanceApprover;
import org.finance.business.mapper.ApprovalInstanceApproverMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批流相关的审核人员 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Service
public class ApprovalInstanceApproverService extends ServiceImpl<ApprovalInstanceApproverMapper, ApprovalInstanceApprover> {

    public List<Long> customerIdsByApproverId(long approverId) {
        return baseMapper.customerIdsByApproverId(approverId);
    }
}
