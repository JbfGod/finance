package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.entity.ExpenseItem;
import org.finance.business.entity.ExpenseItemAttachment;
import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.mapper.ExpenseItemAttachmentMapper;
import org.finance.business.mapper.ExpenseItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.mapper.ExpenseItemSubsidyMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * 费用报销条目 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-14
 */
@Service
public class ExpenseItemService extends ServiceImpl<ExpenseItemMapper, ExpenseItem> {

    @Resource
    private ExpenseItemSubsidyMapper subsidyMapper;
    @Resource
    private ExpenseItemAttachmentMapper attachmentMapper;

    public void deleteByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        baseMapper.deleteBatchIds(ids);
        subsidyMapper.delete(Wrappers.<ExpenseItemSubsidy>lambdaQuery()
                .in(ExpenseItemSubsidy::getItemId, ids)
        );
        attachmentMapper.delete(Wrappers.<ExpenseItemAttachment>lambdaQuery()
                .in(ExpenseItemAttachment::getItemId, ids)
        );
    }
}
