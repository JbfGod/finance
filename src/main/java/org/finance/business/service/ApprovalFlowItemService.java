package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.ApprovalFlowApprover;
import org.finance.business.entity.ApprovalFlowItem;
import org.finance.business.mapper.ApprovalFlowApproverMapper;
import org.finance.business.mapper.ApprovalFlowItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 审批流的审批项 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Service
public class ApprovalFlowItemService extends ServiceImpl<ApprovalFlowItemMapper, ApprovalFlowItem> {

    @Resource
    private ApprovalFlowApproverMapper approvalFlowApproverMapper;

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        ApprovalFlowItem approvalFlowItem = baseMapper.selectById(id);
        Integer level = approvalFlowItem.getLevel();
        baseMapper.deleteById(id);
        approvalFlowApproverMapper.delete(Wrappers.<ApprovalFlowApprover>lambdaQuery()
                .eq(ApprovalFlowApprover::getItemId, id)
        );
        // 重新计算当前审批流级别
        this.update(Wrappers.<ApprovalFlowItem>lambdaUpdate()
            .setSql("level = level - 1")
            .gt(ApprovalFlowItem::getLevel, level)
        );
        this.update(Wrappers.<ApprovalFlowItem>lambdaUpdate()
            .setSql("level = level + 1")
            .lt(ApprovalFlowItem::getLevel, level)
        );
    }

}
