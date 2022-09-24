package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.InitialBalanceItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.InitialBalanceItemMapper;
import org.finance.business.mapper.InitialBalanceMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.function.Consumer;

/**
 * <p>
 * 初始余额 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-08
 */
@Service
public class InitialBalanceService extends ServiceImpl<InitialBalanceMapper, InitialBalance> {

    @Resource
    private InitialBalanceItemMapper itemMapper;

    public void addOrUpdateItem(InitialBalanceItem initialBalanceItem) {
        InitialBalance initialBalance = baseMapper.selectOne(
                Wrappers.<InitialBalance>lambdaQuery().last("limit 1")
        );
        if (initialBalance == null) {
            initialBalance = new InitialBalance().setYear(initialBalanceItem.getYear())
                    .setYearMonthNum(initialBalanceItem.getYearMonthNum());
            baseMapper.insert(initialBalance);
        } else {
            initialBalance.setYear(initialBalanceItem.getYear()).setYearMonthNum(initialBalanceItem.getYearMonthNum());
            baseMapper.updateById(initialBalance);
        }
        initialBalanceItem.setInitialBalanceId(initialBalance.getId());
        if (initialBalanceItem.getId() != null) {
            itemMapper.updateById(initialBalanceItem);
            return;
        }

        itemMapper.insert(initialBalanceItem);
    }

    public InitialBalance auditingByYearMonth(YearMonth yearMonth) {
        InitialBalance initialBalance = createIfNotExists(yearMonth);
        boolean success = this.update(Wrappers.<InitialBalance>lambdaUpdate()
                .set(InitialBalance::getAuditStatus, AuditStatus.AUDITED)
                .eq(InitialBalance::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(InitialBalance::getId, initialBalance.getId()));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
        return initialBalance;
    }

    public void unAuditingById(long initialBalanceId) {
        boolean success = this.update(Wrappers.<InitialBalance>lambdaUpdate()
                .set(InitialBalance::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(InitialBalance::getAuditStatus, AuditStatus.AUDITED)
                .eq(InitialBalance::getId, initialBalanceId));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void bookkeepingById(long initialBalanceId, Consumer<InitialBalance> onSuccess) {
        boolean success = this.update(Wrappers.<InitialBalance>lambdaUpdate()
                .set(InitialBalance::getBookkeeping, true)
                .eq(InitialBalance::getId, initialBalanceId)
                .eq(InitialBalance::getBookkeeping, false)
                .eq(InitialBalance::getAuditStatus, AuditStatus.AUDITED)
        );
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
        onSuccess.accept(baseMapper.selectById(initialBalanceId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void unBookkeepingById(long initialBalanceId, Consumer<InitialBalance> onSuccess) {
        boolean success = this.update(Wrappers.<InitialBalance>lambdaUpdate()
                .set(InitialBalance::getBookkeeping, false)
                .set(InitialBalance::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(InitialBalance::getId, initialBalanceId)
                .eq(InitialBalance::getBookkeeping, true)
                .eq(InitialBalance::getAuditStatus, AuditStatus.AUDITED)
        );
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
        onSuccess.accept(baseMapper.selectById(initialBalanceId));
    }

    private InitialBalance createIfNotExists(YearMonth yearMonth) {
        InitialBalance initialBalance = baseMapper.selectOne(
                Wrappers.<InitialBalance>lambdaQuery().last("limit 1")
        );
        if (initialBalance == null) {
            initialBalance = new InitialBalance().setYear(yearMonth.getYear())
                    .setYearMonthNum(yearMonth.getYear() * 100 + yearMonth.getMonthValue());
            baseMapper.insert(initialBalance);
        }
        return initialBalance;
    }

}
