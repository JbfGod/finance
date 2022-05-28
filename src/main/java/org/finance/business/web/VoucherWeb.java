package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.VoucherConvert;
import org.finance.business.entity.Currency;
import org.finance.business.entity.User;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.service.CustomerService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.service.VoucherService;
import org.finance.business.web.request.AddVoucherRequest;
import org.finance.business.web.request.QueryVoucherItemCueRequest;
import org.finance.business.web.request.QueryVoucherRequest;
import org.finance.business.web.request.UpdateVoucherRequest;
import org.finance.business.web.vo.VoucherDetailVO;
import org.finance.business.web.vo.VoucherPrintContentVO;
import org.finance.business.web.vo.VoucherVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 凭证 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/api/voucher")
public class VoucherWeb {

    @Resource
    private CustomerService customerService;
    @Resource
    private VoucherService baseService;
    @Resource
    private VoucherItemService itemService;
    private final String AUTH_VOUCHER_SEARCH_ALL = "voucher:searchAll";

    @GetMapping("/page")
    public RPage<VoucherVO> pageVoucher(@Valid QueryVoucherRequest request) {
        boolean canSearchAll = SecurityUtil.hasAuthority(AUTH_VOUCHER_SEARCH_ALL);
        User currentUser = SecurityUtil.getCurrentUser();
        boolean isLocalCurrency = request.getCurrencyType() == QueryVoucherRequest.CurrencyType.LOCAL;
        IPage<VoucherVO> page = baseService.page(request.extractPage(), Wrappers.<Voucher>lambdaQuery()
                .eq(isLocalCurrency, Voucher::getCurrencyId, Currency.LOCAL_CURRENCY.getId())
                .gt(!isLocalCurrency, Voucher::getCurrencyId, Currency.LOCAL_CURRENCY.getId())
                .eq(request.getYearMonthNum() != null, Voucher::getYearMonthNum, request.getYearMonthNum())
                .eq(!canSearchAll, Voucher::getCreateBy, currentUser.getId())
        ).convert(VoucherConvert.INSTANCE::toVoucherVO);
        return RPage.build(page);
    }

    @GetMapping("/get/{id}")
    public R<VoucherDetailVO> voucherDetail(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/{id}/printContent")
    public R<VoucherPrintContentVO> printContentOfVoucher(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        VoucherPrintContentVO voucherPrintContentVO = VoucherConvert.INSTANCE.toVoucherPrintContentVO(voucher);
        voucherPrintContentVO.setCustomerName(customerService.getCustomerNameById(voucher.getCustomerId()));
        return R.ok(voucherPrintContentVO);
    }

    @GetMapping("/searchItemCue")
    public R<List<String>> searchVoucherCue(QueryVoucherItemCueRequest request) {
        boolean canSearchAll = SecurityUtil.hasAuthority(AUTH_VOUCHER_SEARCH_ALL);
        User currentUser = SecurityUtil.getCurrentUser();
        QueryVoucherItemCueRequest.Column column = request.getColumn();
        List<String> cues = itemService.list(Wrappers.<VoucherItem>lambdaQuery()
                .select(column.getColumnFunc())
                .eq(!canSearchAll, VoucherItem::getCreateBy, currentUser.getId())
                .likeRight(column.getColumnFunc(), request.getKeyword())
                .groupBy(column.getColumnFunc(), VoucherItem::getModifyTime)
                .orderByDesc(column.getColumnFunc(), VoucherItem::getModifyTime)
                .last(String.format("limit %d", request.getNum()))
        ).stream().map(column.getColumnFunc()).distinct().collect(Collectors.toList());
        return R.ok(cues);
    }

    @PostMapping("/add")
    public R addVoucher(@Valid @RequestBody AddVoucherRequest request) {
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, null);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateVoucher(@Valid @RequestBody UpdateVoucherRequest request) {
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, () -> {
            itemService.removeByIds(request.getDeletedItemIds());
        });
        return R.ok();
    }


}
