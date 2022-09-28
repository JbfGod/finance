package org.finance.business.convert;

import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherBook;
import org.finance.business.entity.VoucherItem;
import org.finance.business.web.request.AddVoucherRequest;
import org.finance.business.web.request.UpdateVoucherRequest;
import org.finance.business.web.vo.VoucherDetailVO;
import org.finance.business.web.vo.VoucherPrintContentVO;
import org.finance.business.web.vo.VoucherItemVO;
import org.finance.business.web.vo.VoucherVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author jiangbangfa
 */
@Mapper
public interface VoucherConvert {

    VoucherConvert INSTANCE = Mappers.getMapper( VoucherConvert.class );

    Voucher toVoucher(AddVoucherRequest request);

    Voucher toVoucher(UpdateVoucherRequest request);

    VoucherBook toVoucherBook(VoucherItem voucher);

    VoucherVO toVoucherVO(Voucher voucher);

    VoucherDetailVO toVoucherDetailVO(Voucher voucher);

    VoucherPrintContentVO toVoucherPrintContentVO(Voucher voucher);

    VoucherItemVO toVoucherItem(VoucherItem item);

    default VoucherItem summary(List<VoucherItem> voucherItems) {
        VoucherItem voucherItem = new VoucherItem();
        BigDecimal zero = new BigDecimal("0");
        for (VoucherItem item : voucherItems) {
            voucherItem.setSubjectId(item.getSubjectId())
                .setSubjectNumber(item.getSubjectNumber())
                .setVoucherNumber(item.getVoucherNumber())
                .setLocalDebitAmount(Optional.ofNullable(voucherItem.getLocalDebitAmount()).orElse(zero).add(item.getLocalDebitAmount()))
                .setLocalCreditAmount(Optional.ofNullable(voucherItem.getLocalCreditAmount()).orElse(zero).add(item.getLocalCreditAmount()));
        }
        return voucherItem;
    }
}
