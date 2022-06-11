package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.VoucherConvert;
import org.finance.business.entity.Currency;
import org.finance.business.entity.User;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.service.CustomerService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.service.VoucherService;
import org.finance.business.web.request.AddVoucherRequest;
import org.finance.business.web.request.AuditingVoucherRequest;
import org.finance.business.web.request.BookkeepingVoucherRequest;
import org.finance.business.web.request.QueryVoucherBookRequest;
import org.finance.business.web.request.QueryVoucherItemCueRequest;
import org.finance.business.web.request.QueryVoucherRequest;
import org.finance.business.web.request.UnBookkeepingVoucherRequest;
import org.finance.business.web.request.UpdateVoucherRequest;
import org.finance.business.web.vo.VoucherBookVO;
import org.finance.business.web.vo.VoucherDetailVO;
import org.finance.business.web.vo.VoucherPrintContentVO;
import org.finance.business.web.vo.VoucherVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final String RESOURCE_TARGET = "voucher";

    @GetMapping("/page")
    public RPage<VoucherVO> pageVoucher(@Valid QueryVoucherRequest request) {
        boolean canSearchAll = SecurityUtil.canViewAll(RESOURCE_TARGET);
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

    @GetMapping("/page/book")
    public RPage<VoucherBookVO> pageVoucherBook(QueryVoucherBookRequest request) {
        return RPage.build(baseService.pageVoucherBookVO(request.extractPage()));
    }

    @GetMapping("/get/{id}")
    public R<VoucherDetailVO> voucherDetail(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/{id}/printContent")
    @PreAuthorize("hasPermission('voucher', 'print')")
    public R<VoucherPrintContentVO> printContentOfVoucher(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        VoucherPrintContentVO voucherPrintContentVO = VoucherConvert.INSTANCE.toVoucherPrintContentVO(voucher);
        voucherPrintContentVO.setCustomerName(customerService.getCustomerNameById(voucher.getCustomerId()));
        return R.ok(voucherPrintContentVO);
    }

    @GetMapping("/searchItemCue")
    public R<List<String>> searchVoucherCue(QueryVoucherItemCueRequest request) {
        boolean canSearchAll = SecurityUtil.canViewAll(RESOURCE_TARGET);
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
    @PreAuthorize("hasPermission('voucher', 'operating')")
    public R addVoucher(@Valid @RequestBody AddVoucherRequest request) {
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, null);
        return R.ok();
    }

    @PutMapping("/auditing/{id}")
    @PreAuthorize("hasPermission('voucher', 'auditing')")
    public R auditingVoucher(@PathVariable("id") long id) {
        baseService.auditingById(id);
        return R.ok();
    }

    @PutMapping("/unAuditing/{id}")
    @PreAuthorize("hasPermission('voucher', 'unAuditing')")
    public R unAuditingVoucher(@PathVariable("id") long id) {
        baseService.unAuditingById(id);
        return R.ok();
    }

    @PutMapping("/auditing")
    @PreAuthorize("hasPermission('voucher:batch', 'auditing')")
    public R batchAuditingVoucher(@Valid AuditingVoucherRequest request) {
        Long customerId = SecurityUtil.getCustomerIdFromRequest();
        baseService.batchAuditingVoucher(request.getYearMonth(), request.getBeginSerialNum(),
                request.getEndSerialNum(), customerId);
        return R.ok();
    }

    @PutMapping("/unAuditing")
    @PreAuthorize("hasPermission('voucher:batch', 'unAuditing')")
    public R batchUnAuditingVoucher(@Valid AuditingVoucherRequest request) {
        Long customerId = SecurityUtil.getCustomerIdFromRequest();
        baseService.batchUnAuditingVoucher(request.getYearMonth(), request.getBeginSerialNum(),
                request.getEndSerialNum(), customerId);
        return R.ok();
    }

    @PutMapping("/bookkeeping/{id}")
    @PreAuthorize("hasPermission('voucher', 'bookkeeping')")
    public R bookkeepingVoucher(@PathVariable("id") long id) {
        baseService.bookkeepingById(id);
        return R.ok();
    }

    @PutMapping("/unBookkeeping/{id}")
    @PreAuthorize("hasPermission('voucher', 'unBookkeeping')")
    public R unBookkeepingVoucher(@PathVariable("id") long id) {
        baseService.unBookkeepingById(id);
        return R.ok();
    }

    @PutMapping("/bookkeeping")
    @PreAuthorize("hasPermission('voucher:batch', 'bookkeeping')")
    public R batchBookkeepingVoucher(@Valid BookkeepingVoucherRequest request) {
        Long customerId = SecurityUtil.getCustomerIdFromRequest();
        baseService.batchBookkeepingVoucher(request.getYearMonth(), request.getBeginSerialNum(),
                request.getEndSerialNum(), customerId);
        return R.ok();
    }

    @PutMapping("/unBookkeeping")
    @PreAuthorize("hasPermission('voucher:batch', 'unBookkeeping')")
    public R batchUnBookkeepingVoucher(@Valid UnBookkeepingVoucherRequest request) {
        Long customerId = SecurityUtil.getCustomerIdFromRequest();
        baseService.batchUnBookkeepingVoucher(request.getYearMonth(), request.getBeginSerialNum(),
                request.getEndSerialNum(), customerId);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('voucher', 'operating')")
    public R deleteVoucher(@PathVariable("id") long id) {
        assertUnAudited(id);
        baseService.deleteById(id);
        return R.ok();
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('voucher', 'operating')")
    public R updateVoucher(@Valid @RequestBody UpdateVoucherRequest request) {
        assertUnAudited(request.getId());
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, () -> {
            itemService.removeByIds(request.getDeletedItemIds());
        });
        return R.ok();
    }

    private void assertUnAudited(long voucherId) {
        boolean unAudited = baseService.count(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getId, voucherId)
                .eq(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
        ) > 0;
        AssertUtil.isTrue(unAudited, "操作失败，该记录已经审核！");
    }
}
