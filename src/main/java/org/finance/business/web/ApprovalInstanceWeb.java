package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.ApprovalConvert;
import org.finance.business.entity.ApprovalFlow;
import org.finance.business.entity.ApprovalInstance;
import org.finance.business.entity.ApprovalInstanceApprover;
import org.finance.business.entity.ApprovalInstanceItem;
import org.finance.business.entity.ExpenseBill;
import org.finance.business.entity.ExpenseItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.service.ApprovalInstanceApproverService;
import org.finance.business.service.ApprovalInstanceItemService;
import org.finance.business.service.ApprovalInstanceService;
import org.finance.business.service.ExpenseBillService;
import org.finance.business.service.ExpenseItemService;
import org.finance.business.service.VoucherService;
import org.finance.business.web.request.ApprovedRequest;
import org.finance.business.web.request.ReviewRejectedRequest;
import org.finance.business.web.vo.ApprovalInstanceItemVO;
import org.finance.business.web.vo.ApprovalInstanceVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 审批实例 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/api/approvalInstance")
public class ApprovalInstanceWeb {

    @Resource
    private ApprovalInstanceService baseService;
    @Resource
    private ApprovalInstanceItemService approvalInstanceItemService;
    @Resource
    private ApprovalInstanceApproverService instanceApproverService;
    @Resource
    private ExpenseBillService expenseBillService;
    @Resource
    private ExpenseItemService expenseItemService;
    @Resource
    private VoucherService voucherService;

    @GetMapping("/{id}")
    public R<ApprovalInstanceVO> approvalInstance(@PathVariable("id") long id) {
        ApprovalInstance approvalInstance = baseService.getById(id);
        approvalInstance.setItems(
            approvalInstanceItemService.list(Wrappers.<ApprovalInstanceItem>lambdaQuery()
                .eq(ApprovalInstanceItem::getInstanceId, id)
            )
        );
        Long userId = SecurityUtil.getCurrentUserId();
        ApprovalInstanceVO approvalInstanceVO = ApprovalConvert.INSTANCE.toApprovalInstanceVO(approvalInstance);
        List<ApprovalInstanceItemVO> items = approvalInstanceVO.getItems();
        // 标记当前操作用户能够审批的该流程项，仅匹配一项即可
        for (ApprovalInstanceItemVO item : items) {
            if (!item.getLevel().equals(approvalInstance.getCurrentLevel())) {
                continue;
            }
            boolean canApproved = instanceApproverService.count(Wrappers.<ApprovalInstanceApprover>lambdaQuery()
                    .eq(ApprovalInstanceApprover::getItemId, item.getId())
                    .eq(ApprovalInstanceApprover::getApproverId, userId)
            ) > 0;
            item.setCanApproved(canApproved);
            if (canApproved) {
                break;
            }
        }
        return R.ok(approvalInstanceVO);
    }

    @PutMapping("/approved")
    public R approved(@Valid @RequestBody ApprovedRequest approvedRequest) {
        baseService.approved(approvedRequest, this::onApproved);
        return R.ok();
    }

    @PutMapping("/rejected")
    public R reviewRejected(@Valid @RequestBody ReviewRejectedRequest rejectedRequest) {
        baseService.reviewRejected(rejectedRequest, this::onReviewRejected);
        return R.ok();
    }

    private void onApproved(ApprovalInstance approvalInstance, ApprovalInstanceItem approvalInstanceItem) {
        ApprovalFlow.BusinessModule businessModule = approvalInstance.getBusinessModule();
        if (businessModule == ApprovalFlow.BusinessModule.EXPENSE_BILL) {
            if (approvalInstanceItem.getLasted()) {
                expenseBillService.update(Wrappers.<ExpenseBill>lambdaUpdate()
                    .eq(ExpenseBill::getId, approvalInstance.getModuleId())
                    .set(ExpenseBill::getAuditStatus, AuditStatus.APPROVED)
                );
                ExpenseBill expenseBill = expenseBillService.getById(approvalInstance.getModuleId());
                List<ExpenseItem> items = expenseItemService.list(Wrappers.<ExpenseItem>lambdaQuery()
                    .eq(ExpenseItem::getBillId, expenseBill.getId())
                );
                expenseBill.setItems(items);
                voucherService.addOrUpdate(expenseBill.toVoucher(), null);
            }
        }
    }

    /**
     * @return true: 阻断执行
     */
    private boolean onReviewRejected(ApprovalInstance approvalInstance, ApprovalInstanceItem approvalInstanceItem) {
        ApprovalFlow.BusinessModule businessModule = approvalInstance.getBusinessModule();
        if (businessModule == ApprovalFlow.BusinessModule.EXPENSE_BILL) {
            if (approvalInstanceItem.getLevel() == 1) {
                boolean success = expenseBillService.update(Wrappers.<ExpenseBill>lambdaUpdate()
                    .eq(ExpenseBill::getId, approvalInstance.getModuleId())
                    .eq(ExpenseBill::getAuditStatus, AuditStatus.AUDITED)
                    .set(ExpenseBill::getApprovalFlowInstanceId, 0)
                    .set(ExpenseBill::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                );
                AssertUtil.isTrue(success, "操作失败，该流程项状态已发生变化！");
                baseService.removeById(approvalInstance.getId());
                approvalInstanceItemService.remove(Wrappers.<ApprovalInstanceItem>lambdaQuery()
                    .eq(ApprovalInstanceItem::getInstanceId, approvalInstance.getId())
                );
                instanceApproverService.remove(Wrappers.<ApprovalInstanceApprover>lambdaQuery()
                    .eq(ApprovalInstanceApprover::getInstanceId, approvalInstance.getId())
                );
                return true;
            }
        }
        return false;
    }
}
