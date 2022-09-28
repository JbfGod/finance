package org.finance.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.convert.VoucherConvert;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.mapper.param.QueryVoucherItemOfSubLegerParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 凭证项 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Service
public class VoucherItemService extends ServiceImpl<VoucherItemMapper, VoucherItem> {

    public List<VoucherItem> summaryGroupBySubjectId(int yearMonthNum) {
        return baseMapper.summaryGroupBySubjectId(yearMonthNum);
    }

    public List<VoucherItem> summaryByCurrencyGroupBySubjectId(int yearMonthNum, String currencyName) {
        return baseMapper.summaryByCurrencyGroupBySubjectId(yearMonthNum, currencyName);
    }

    public  List<VoucherItem> listByMonthAndCurrency(QueryVoucherItemOfSubLegerParam param) {
        return baseMapper.listByMonthAndCurrency(param);
    }

    public List<VoucherItem> summaryVoucherItem(QueryVoucherItemOfSubLegerParam param, Consumer<VoucherItem> forEachItem) {
        List<VoucherItem> voucherItems = baseMapper.listByMonthAndCurrency(param);
        Map<Long, List<VoucherItem>> voucherItemsBySubjectId = voucherItems.stream().collect(Collectors.groupingBy(VoucherItem::getSubjectId));
        Collection<List<VoucherItem>> voucherItemArrays = voucherItemsBySubjectId.values();
        List<VoucherItem> summaryVoucherItems = new ArrayList<>();
        voucherItemArrays.stream().flatMap(voucherItemArray -> {
            summaryVoucherItems.add(VoucherConvert.INSTANCE.summary(voucherItemArray));
            return voucherItemArray.stream();
        }).sorted((curr, next) -> {
            int orderByVoucherDate = curr.getVoucherDate().compareTo(next.getVoucherDate());
            if (orderByVoucherDate == 0) {
                return curr.getVoucherNumber().compareTo(next.getVoucherNumber());
            }
            return  orderByVoucherDate;
        }).forEach(forEachItem);

        return summaryVoucherItems;
    }
}
