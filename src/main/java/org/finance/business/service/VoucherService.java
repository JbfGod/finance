package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.CurrencyMapper;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.mapper.VoucherMapper;
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
    @Resource
    private CurrencyMapper currencyMapper;

    public Voucher getAndItemsById(long voucherId) {
        Voucher voucher = baseMapper.selectById(voucherId);
        List<VoucherItem> items = itemMapper.selectList(Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, voucherId));
        voucher.setItems(items);
        return voucher;
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
        }
        itemMapper.insert(item);
    }

}
