package org.finance.business.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Currency;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.CurrencyMapper;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.mapper.VoucherMapper;
import org.finance.business.web.vo.VoucherBookVO;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 凭证 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Service
public class VoucherService extends ServiceImpl<VoucherMapper, Voucher> {

    @Resource
    private VoucherItemMapper itemMapper;
    @Resource
    private CurrencyMapper currencyMapper;

    public Voucher getAndItemsById(long voucherId) {
        return baseMapper.selectById(voucherId).loadItems(itemMapper);
    }

    public Voucher getNextAndItems(long serialNumber, int period) {
        return baseMapper.selectOne(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, period)
                .gt(Voucher::getSerialNumber, serialNumber)
                .orderByAsc(Voucher::getSerialNumber)
                .last("limit 1")
        );
    }

    public Voucher getPrevAndItems(long serialNumber, int period) {
        return baseMapper.selectOne(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, period)
                .lt(Voucher::getSerialNumber, serialNumber)
                .orderByDesc(Voucher::getSerialNumber)
                .last("limit 1")
        );
    }

    public Voucher getFirstAndItems(int period) {
        return baseMapper.selectOne(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, period)
                .orderByAsc(Voucher::getSerialNumber)
                .last("limit 1")
        );
    }

    public Voucher getLastAndItems(int period) {
        return baseMapper.selectOne(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, period)
                .orderByDesc(Voucher::getSerialNumber)
                .last("limit 1")
        );
    }

    public IPage<VoucherBookVO> pageVoucherBookVO(IPage<?> page) {
        return baseMapper.selectVoucherBookVO(page);
    }

    public Integer getMaxSerialNumberByYearMonth(int yearMonthNum) {
        return Optional.ofNullable(
                baseMapper.getMaxSerialNumber(yearMonthNum)
        ).map(num -> num + 1).orElse(1);
    }

    @Transactional(rollbackFor = Exception.class)
    public Voucher addOrUpdate(Voucher voucher, Runnable beforeCall) {
        if (beforeCall != null) {
            beforeCall.run();
        }

        this.addOrUpdateVoucher(voucher);

        List<VoucherItem> items = voucher.getItems();
        int itemSize = items.size();
        Currency currency = voucher.getCurrencyId().equals(Currency.LOCAL_CURRENCY.getId())
                ? Currency.LOCAL_CURRENCY
                : currencyMapper.selectById(voucher.getCurrencyId());
        for (int i = 0; i < itemSize; i++) {
            VoucherItem item = items.get(i).setSerialNumber(i + 1);
            item.setLocalDebitAmount(item.getDebitAmount().multiply(currency.getRate()))
                    .setLocalCreditAmount(item.getCreditAmount().multiply(currency.getRate()))
                    .setVoucherNumber(voucher.getSerialNumber())
                    .setVoucherDate(voucher.getVoucherDate());
            this.addOrUpdateItem(voucher, item);
        }
        return voucher;
    }

    public void auditingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .set(Voucher::getAuditBy, SecurityUtil.getUserId())
                .set(Voucher::getAuditorName, SecurityUtil.getUserName())
                .eq(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    public void unAuditingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .set(Voucher::getAuditBy, 0L)
                .set(Voucher::getAuditorName, "")
                .eq(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchAuditingVoucher(List<Long> ids) {
        this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .set(Voucher::getAuditBy, SecurityUtil.getUserId())
                .set(Voucher::getAuditorName, SecurityUtil.getUserName())
                .eq(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .in(Voucher::getId, ids));
    }

    public void batchUnAuditingVoucher(List<Long> ids) {
        this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .set(Voucher::getAuditBy, 0L)
                .set(Voucher::getAuditorName, "")
                .eq(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .in(Voucher::getId, ids));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long voucherId) {
        baseMapper.deleteById(voucherId);
        itemMapper.delete(Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, voucherId));

        // TODO 重置当月凭证的序列号
        /*List<Voucher> dbVouchers = baseMapper.selectList(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, voucher.getYearMonthNum())
                .orderByAsc(Voucher::getCreateBy)
        );
        for (int i = 0; i < dbVouchers.size(); i++) {
            this.update(Wrappers.<Voucher>lambdaUpdate()
                    .set(Voucher::getSerialNumber, i + 1)
                    .eq(Voucher::getId, dbVouchers.get(i).getId())
            );
        }*/
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> voucherIds) {
        baseMapper.delete(Wrappers.<Voucher>lambdaQuery().in(Voucher::getId, voucherIds));
        itemMapper.delete(Wrappers.<VoucherItem>lambdaQuery().in(VoucherItem::getVoucherId, voucherIds));
    }

    private void addOrUpdateVoucher(Voucher voucher) {
        if (voucher.getSerialNumber() == null) {
            Integer serialNumber = Optional.ofNullable(
                    baseMapper.getMaxSerialNumber(voucher.getYearMonthNum())
            ).map(num -> num + 1).orElse(1);
            voucher.setSerialNumber(serialNumber);
        }
        if (voucher.getId() != null) {
            baseMapper.updateById(voucher);
            return;
        }
        baseMapper.insert(voucher);
    }

    private void addOrUpdateItem(Voucher voucher, VoucherItem item) {
        item.setVoucherId(voucher.getId())
                .setYear(voucher.getYear())
                .setYearMonthNum(voucher.getYearMonthNum());
        if (item.getId() != null) {
            itemMapper.updateById(item);
            return;
        }
        itemMapper.insert(item);
    }

    private LambdaUpdateWrapper<Voucher> buildLambdaUpdateByYearMonthAndSerialNum(
            Integer yearMonth, Integer beginSerialNum, Integer endSerialNum, Long customerId) {
        return Wrappers.<Voucher>lambdaUpdate()
                .eq(Voucher::getYearMonthNum, yearMonth)
                .eq(Voucher::getCustomerId, customerId)
                .between(beginSerialNum != null && endSerialNum != null,
                        Voucher::getSerialNumber, beginSerialNum, endSerialNum);
    }

    /**
     * 断言凭证的借贷金额平衡
     */
    private void assertVoucherItemLoanBalanced(long voucherId, int voucherSerialNum) {
        Voucher voucher = baseMapper.selectById(voucherId);
        BigDecimal diff = voucher.getTotalDebitAmount().subtract(voucher.getTotalCreditAmount()).abs();
        AssertUtil.isTrue(voucher.getTotalDebitAmount().equals(voucher.getTotalCreditAmount()),
                String.format("凭证号：%d，借贷金额不平衡，借-贷相差:%s", voucherSerialNum, diff.toString().replaceFirst("\\.?0*$", ""))
        );
    }

}
