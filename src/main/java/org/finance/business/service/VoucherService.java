package org.finance.business.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.mapper.VoucherMapper;
import org.finance.business.web.vo.VoucherBookVO;
import org.finance.infrastructure.exception.HxException;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    public Voucher getAndItemsById(long voucherId) {
        Voucher voucher = baseMapper.selectById(voucherId);
        List<VoucherItem> items = itemMapper.selectList(Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, voucherId));
        voucher.setItems(items);
        return voucher;
    }

    public IPage<VoucherBookVO> pageVoucherBookVO(IPage<?> page) {
        return baseMapper.selectVoucherBookVO(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(Voucher voucher, Runnable beforeCall) {
        if (beforeCall != null) {
            beforeCall.run();
        }

        this.addOrUpdateVoucher(voucher);

        List<VoucherItem> items = voucher.getItems();
        for (VoucherItem item : items) {
            this.addOrUpdateItem(voucher, item);
        }
    }

    public void auditingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .eq(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "该凭证已审核，请尝试刷新记录！");
    }

    public void unAuditingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "该凭证未审核，请尝试刷新记录！");
    }

    public void batchAuditingVoucher(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        this.update(this.buildLambdaUpdateByYearMonthAndSerialNum(
                yearMonth, beginSerialNum, endSerialNum
        ).set(Voucher::getAuditStatus, AuditStatus.AUDITED));
    }

    public void batchUnAuditingVoucher(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        this.update(this.buildLambdaUpdateByYearMonthAndSerialNum(
                yearMonth, beginSerialNum, endSerialNum
        ).set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED));
    }

    public void bookkeepingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getBookkeeping, true)
                .eq(Voucher::getBookkeeping, false)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "该凭证已记账，请尝试刷新记录！");
    }

    public void unBookkeepingById(long voucherId) {
        boolean success = this.update(Wrappers.<Voucher>lambdaUpdate()
                .set(Voucher::getBookkeeping, false)
                .set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(Voucher::getBookkeeping, true)
                .eq(Voucher::getId, voucherId));
        AssertUtil.isTrue(success, "该凭证未记账，请尝试刷新记录！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchBookkeepingVoucher(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        this.update(this.buildLambdaUpdateByYearMonthAndSerialNum(
                yearMonth, beginSerialNum, endSerialNum
        ).set(Voucher::getBookkeeping, true));
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUnBookkeepingVoucher(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        this.update(this.buildLambdaUpdateByYearMonthAndSerialNum(
                yearMonth, beginSerialNum, endSerialNum
        ).set(Voucher::getBookkeeping, false).set(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long voucherId) {
        Voucher voucher = baseMapper.selectById(voucherId);
        if (voucher.getAuditStatus() == AuditStatus.AUDITED) {
            throw new HxException("该凭证已审核，不能删除！");
        }
        baseMapper.deleteById(voucherId);
        itemMapper.delete(Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, voucherId));

        // 重置当月凭证的序列号
        List<Voucher> dbVouchers = baseMapper.selectList(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, voucher.getYearMonthNum())
                .orderByAsc(Voucher::getCreateBy)
        );
        for (int i = 0; i < dbVouchers.size(); i++) {
            this.update(Wrappers.<Voucher>lambdaUpdate()
                    .set(Voucher::getSerialNumber, i + 1)
                    .eq(Voucher::getId, dbVouchers.get(i).getId())
            );
        }
    }

    private void addOrUpdateVoucher(Voucher voucher) {
        if (voucher.getId() != null) {
            baseMapper.updateById(voucher);
            return;
        }
        Integer serialNumber = Optional.ofNullable(
            baseMapper.getMaxSerialNumber(voucher.getYearMonthNum())
        ).map(num -> num + 1).orElse(1);
        voucher.setSerialNumber(serialNumber);

        // TODO 校验序列号唯一
        baseMapper.insert(voucher);
    }

    private void addOrUpdateItem(Voucher voucher, VoucherItem item) {
        item.setVoucherId(voucher.getId())
            .setYear(voucher.getYear())
            .setYearMonthNum(voucher.getYearMonthNum())
            .setCurrencyId(voucher.getCurrencyId())
            .setCurrencyName(voucher.getCurrencyName())
            .setRate(voucher.getRate());
        if (item.getId() != null) {
            itemMapper.updateById(item);
            return;
        }
        itemMapper.insert(item);
    }

    private LambdaUpdateWrapper<Voucher> buildLambdaUpdateByYearMonthAndSerialNum(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        return Wrappers.<Voucher>lambdaUpdate()
                .eq(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .eq(Voucher::getYearMonthNum, yearMonth)
                .between(beginSerialNum != null && endSerialNum != null,
                        Voucher::getSerialNumber, beginSerialNum, endSerialNum);
    }

}
