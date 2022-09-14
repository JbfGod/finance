package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.ApprovalFlow;
import org.finance.business.entity.ApprovalFlowApprover;
import org.finance.business.entity.ApprovalFlowItem;
import org.finance.business.entity.ApprovalInstance;
import org.finance.business.entity.ApprovalInstanceApprover;
import org.finance.business.entity.ApprovalInstanceItem;
import org.finance.business.mapper.ApprovalFlowApproverMapper;
import org.finance.business.mapper.ApprovalFlowItemMapper;
import org.finance.business.mapper.ApprovalFlowMapper;
import org.finance.business.mapper.ApprovalInstanceApproverMapper;
import org.finance.business.mapper.ApprovalInstanceItemMapper;
import org.finance.business.mapper.ApprovalInstanceMapper;
import org.finance.business.web.request.AddApprovalFlowRequest;
import org.finance.business.web.request.SaveApprovalFlowRequest;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 审批流 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-05
 */
@Service
public class ApprovalFlowService extends ServiceImpl<ApprovalFlowMapper, ApprovalFlow> {

    @Resource
    private ApprovalFlowItemMapper flowItemMapper;
    @Resource
    private ApprovalFlowApproverMapper flowApproverMapper;
    @Resource
    private ApprovalInstanceMapper instanceMapper;
    @Resource
    private ApprovalInstanceItemMapper instanceItemMapper;
    @Resource
    private ApprovalInstanceApproverMapper instanceApproverMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(SaveApprovalFlowRequest request) {
        Long customerId = request.getCustomerId();
        ApprovalFlow.BusinessModule businessModule = request.getBusinessModule();

        ApprovalFlow approvalFlow = baseMapper.selectOne(Wrappers.<ApprovalFlow>lambdaQuery()
                .eq(ApprovalFlow::getCustomerId, customerId)
                .eq(ApprovalFlow::getBusinessModule, businessModule)
        );
        if (approvalFlow == null) {
            // 初始化审批流程
            approvalFlow = new ApprovalFlow().setCustomerId(customerId).setBusinessModule(businessModule);
            baseMapper.insert(approvalFlow);
        }
        flowItemMapper.delete(Wrappers.<ApprovalFlowItem>lambdaQuery()
                .eq(ApprovalFlowItem::getApprovalFlowId, approvalFlow.getId())
        );
        flowApproverMapper.delete(Wrappers.<ApprovalFlowApprover>lambdaQuery()
                .eq(ApprovalFlowApprover::getFlowId, approvalFlow.getId())
        );
        List<AddApprovalFlowRequest.FlowItem> flowItems = request.getFlowItems();
        int flowItemSize = flowItems.size();
        for (int i = 0; i < flowItemSize; i++) {
            AddApprovalFlowRequest.FlowItem flowItem = flowItems.get(i);
            ApprovalFlowItem approvalFlowItem = new ApprovalFlowItem()
                    .setDepartment(flowItem.getDepartment())
                    .setApprovalFlowId(approvalFlow.getId())
                    .setLasted(i == flowItemSize - 1)
                    .setLevel(i + 1);
            flowItemMapper.insert(approvalFlowItem);
            List<Long> approverIds = flowItem.getApproverIds();
            for (Long approverId : approverIds) {
                flowApproverMapper.insert(new ApprovalFlowApprover()
                        .setFlowId(approvalFlow.getId())
                        .setItemId(approvalFlowItem.getId())
                        .setApproverId(approverId)
                );
            }
        }
    }

    /**
     * 创建流程实例
     * @return 实例ID
     */
    @Transactional(rollbackFor = Exception.class)
    public long newInstance(long customerId, long moduleId, ApprovalFlow.BusinessModule businessModule) {
        ApprovalFlow approvalFlow = baseMapper.selectOne(Wrappers.<ApprovalFlow>lambdaQuery()
            .eq(ApprovalFlow::getCustomerId, customerId)
            .eq(ApprovalFlow::getBusinessModule, businessModule)
        );
        AssertUtil.notNull(approvalFlow, "当前客户单位暂未设置审批流程，请联系管理员！");
        // 创建审批流程实例
        ApprovalInstance approvalInstance = new ApprovalInstance(approvalFlow);
        approvalInstance.setModuleId(moduleId);
        instanceMapper.insert(approvalInstance);

        List<ApprovalFlowItem> approvalFlowItems = flowItemMapper.selectList(Wrappers.<ApprovalFlowItem>lambdaQuery()
            .eq(ApprovalFlowItem::getApprovalFlowId, approvalFlow.getId())
        );
        Long currentInstanceItemId = null;
        for (ApprovalFlowItem approvalFlowItem : approvalFlowItems) {
            ApprovalInstanceItem approvalInstanceItem = new ApprovalInstanceItem(approvalInstance, approvalFlowItem);
            instanceItemMapper.insert(approvalInstanceItem);
            if (currentInstanceItemId == null) {
                currentInstanceItemId = approvalInstanceItem.getId();
            }
            flowApproverMapper.selectList(Wrappers.<ApprovalFlowApprover>lambdaQuery()
                .eq(ApprovalFlowApprover::getFlowId, approvalFlow.getId())
                .eq(ApprovalFlowApprover::getItemId, approvalFlowItem.getId())
            ).forEach(flowApprover -> {
                instanceApproverMapper.insert(
                        new ApprovalInstanceApprover()
                                .setModuleId(moduleId)
                                .setInstanceId(approvalInstance.getId())
                                .setItemId(approvalInstanceItem.getId())
                                .setCustomerId(approvalFlow.getCustomerId())
                                .setApproverId(flowApprover.getApproverId())
                );
            });
        }
        instanceMapper.update(null, Wrappers.<ApprovalInstance>lambdaUpdate()
                .set(ApprovalInstance::getCurrentItemId, currentInstanceItemId)
                .eq(ApprovalInstance::getId, approvalInstance.getId())
        );
        return approvalInstance.getId();
    }
}
