package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.ApprovalConvert;
import org.finance.business.entity.ApprovalFlow;
import org.finance.business.entity.ApprovalFlowApprover;
import org.finance.business.entity.ApprovalFlowItem;
import org.finance.business.service.ApprovalFlowApproverService;
import org.finance.business.service.ApprovalFlowItemService;
import org.finance.business.service.ApprovalFlowService;
import org.finance.business.web.request.QueryApprovalFlowItemRequest;
import org.finance.business.web.request.SaveApprovalFlowRequest;
import org.finance.business.web.vo.ApprovalFlowItemVO;
import org.finance.infrastructure.common.R;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批流 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/api/approvalFlow")
public class ApprovalFlowWeb {

    @Resource
    private ApprovalFlowService baseService;
    @Resource
    private ApprovalFlowItemService flowItemService;
    @Resource
    private ApprovalFlowApproverService flowApproverService;

    @GetMapping("/flowItems")
    public R<List<ApprovalFlowItemVO>> flowItems(@Valid QueryApprovalFlowItemRequest request) {
        ApprovalFlow approvalFlow = baseService.getOne(Wrappers.<ApprovalFlow>lambdaQuery()
            .eq(ApprovalFlow::getCustomerId, request.getCustomerId())
            .eq(ApprovalFlow::getBusinessModule, request.getBusinessModule())
        );
        if (approvalFlow == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(
            flowItemService.list(Wrappers.<ApprovalFlowItem>lambdaQuery()
                .eq(ApprovalFlowItem::getApprovalFlowId, approvalFlow.getId())
                .orderByAsc(ApprovalFlowItem::getLevel)
            ).stream().map(flowItem -> {
                ApprovalFlowItemVO approvalFlowItemVO = ApprovalConvert.INSTANCE.toApprovalFlowItemVO(flowItem);
                List<Long> approverIds = flowApproverService.list(Wrappers.<ApprovalFlowApprover>lambdaQuery()
                        .eq(ApprovalFlowApprover::getItemId, flowItem.getId())
                ).stream().map(ApprovalFlowApprover::getApproverId).collect(Collectors.toList());
                approvalFlowItemVO.setApproverIds(approverIds);
                return approvalFlowItemVO;
            }).collect(Collectors.toList())
        );
    }

    @PostMapping("/saveFlowItem")
    public R saveFlowItem(@RequestBody @Valid SaveApprovalFlowRequest request) {
        baseService.save(request);
        return R.ok();
    }

    @DeleteMapping("/flowItem/{id}")
    public R deleteFlowItem(@PathVariable("id") long id) {
        flowItemService.delete(id);
        return R.ok();
    }

}
