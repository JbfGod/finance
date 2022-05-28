package org.finance.business.convert;

import org.finance.business.entity.Voucher;
import org.finance.business.web.request.AddVoucherRequest;
import org.finance.business.web.request.UpdateVoucherRequest;
import org.finance.business.web.vo.VoucherDetailVO;
import org.finance.business.web.vo.VoucherPrintContentVO;
import org.finance.business.web.vo.VoucherVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jiangbangfa
 */
@Mapper
public interface VoucherConvert {

    VoucherConvert INSTANCE = Mappers.getMapper( VoucherConvert.class );

    Voucher toVoucher(AddVoucherRequest request);

    Voucher toVoucher(UpdateVoucherRequest request);

    VoucherVO toVoucherVO(Voucher voucher);

    VoucherDetailVO toVoucherDetailVO(Voucher voucher);

    VoucherPrintContentVO toVoucherPrintContentVO(Voucher voucher);

}
