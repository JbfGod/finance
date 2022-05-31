package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Currency;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.CurrencyMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 货币汇率列表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Service
public class CurrencyService extends ServiceImpl<CurrencyMapper, Currency> {

    public void auditingById(long id) {
        boolean success = this.update(Wrappers.<Currency>lambdaUpdate()
                .set(Currency::getAuditStatus, AuditStatus.AUDITED)
                .eq(Currency::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Currency::getId, id));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    public void unAuditingById(long id) {
        boolean success = this.update(Wrappers.<Currency>lambdaUpdate()
                .set(Currency::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Currency::getAuditStatus, AuditStatus.AUDITED)
                .eq(Currency::getId, id));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

}
