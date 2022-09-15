package org.finance.business.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.ApprovalInstance;
import org.finance.business.entity.ApprovalInstanceItem;
import org.finance.business.entity.User;
import org.finance.business.mapper.ApprovalInstanceItemMapper;
import org.finance.business.mapper.ApprovalInstanceMapper;
import org.finance.business.web.request.ApprovedRequest;
import org.finance.business.web.request.ReviewRejectedRequest;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * <p>
 * 审批实例 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Service
public class ApprovalInstanceService extends ServiceImpl<ApprovalInstanceMapper, ApprovalInstance> {

    @Resource
    private ApprovalInstanceItemMapper itemMapper;

    @Transactional(rollbackFor = Exception.class)
    public void approved(ApprovedRequest approvedRequest, BiConsumer<ApprovalInstance, ApprovalInstanceItem> onApproved) {
        ApprovalInstanceItem approvalInstanceItem = itemMapper.selectById(approvedRequest.getApprovalInstanceItemId());
        User currentUser = SecurityUtil.getCurrentUser();
        int updateCount = itemMapper.update(null, Wrappers.<ApprovalInstanceItem>lambdaUpdate()
                .eq(ApprovalInstanceItem::getId, approvedRequest.getApprovalInstanceItemId())
                .eq(ApprovalInstanceItem::getPassed, false)
                .set(ApprovalInstanceItem::getApproverId, currentUser.getId())
                .set(ApprovalInstanceItem::getApprover, currentUser.getName())
                .set(ApprovalInstanceItem::getApprovalTime, LocalDateTime.now())
                .set(ApprovalInstanceItem::getRemark, approvedRequest.getRemark())
                .set(ApprovalInstanceItem::getPassed, true)
        );
        AssertUtil.isTrue(updateCount > 0, "操作失败，该流程项状态已发生变化！");
        ApprovalInstance approvalInstance = baseMapper.selectById(approvalInstanceItem.getInstanceId());
        LambdaUpdateWrapper<ApprovalInstance> updateWrapper = Wrappers.<ApprovalInstance>lambdaUpdate()
                .eq(ApprovalInstance::getId, approvalInstance.getId())
                .set(ApprovalInstance::getCurrentItemId, approvalInstanceItem.getId())
                .set(!approvalInstanceItem.getLasted(), ApprovalInstance::getCurrentLevel, approvalInstanceItem.getLevel() + 1);
        if (approvalInstanceItem.getLasted()) {
            updateWrapper = updateWrapper.set(ApprovalInstance::getEnded, true);
        }
        baseMapper.update(null, updateWrapper);

        onApproved.accept(approvalInstance, approvalInstanceItem);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reviewRejected(ReviewRejectedRequest rejectedRequest, BiFunction<ApprovalInstance, ApprovalInstanceItem, Boolean> onRejected) {
        ApprovalInstanceItem instanceItem = itemMapper.selectById(rejectedRequest.getApprovalInstanceItemId());
        ApprovalInstance approvalInstance = baseMapper.selectById(instanceItem.getInstanceId());

        Boolean isInterrupt = onRejected.apply(approvalInstance, instanceItem);
        if (isInterrupt) {
            return;
        }
        User currentUser = SecurityUtil.getCurrentUser();
        // 更新当前流程实例所处审批级别
        baseMapper.update(null, Wrappers.<ApprovalInstance>lambdaUpdate()
                .eq(ApprovalInstance::getId, approvalInstance.getId())
                .set(ApprovalInstance::getCurrentItemId, rejectedRequest.getPreviousApprovalInstanceItemId())
                .set(ApprovalInstance::getCurrentLevel, instanceItem.getLevel() - 1));
        // 更新上一级审批通过状态为false
        int updateCount = itemMapper.update(null, Wrappers.<ApprovalInstanceItem>lambdaUpdate()
                .eq(ApprovalInstanceItem::getId, rejectedRequest.getPreviousApprovalInstanceItemId())
                .eq(ApprovalInstanceItem::getPassed, true)
                .set(ApprovalInstanceItem::getPassed, false)
        );
        AssertUtil.isTrue(updateCount > 0, "操作失败，该流程项状态已发生变化！");
        // 更新当前审批项的信息
        itemMapper.update(null, Wrappers.<ApprovalInstanceItem>lambdaUpdate()
                .eq(ApprovalInstanceItem::getId, instanceItem.getId())
                .set(ApprovalInstanceItem::getApproverId, currentUser.getId())
                .set(ApprovalInstanceItem::getApprover, currentUser.getName())
                .set(ApprovalInstanceItem::getApprovalTime, LocalDateTime.now())
                .set(ApprovalInstanceItem::getRemark, rejectedRequest.getRemark())
        );
    }

}
