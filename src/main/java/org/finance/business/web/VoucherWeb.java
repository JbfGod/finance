package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.VoucherConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.User;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.VoucherItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.service.AccountCloseListService;
import org.finance.business.service.CustomerService;
import org.finance.business.service.InitialBalanceService;
import org.finance.business.service.SubjectService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.service.VoucherService;
import org.finance.business.web.request.AddVoucherRequest;
import org.finance.business.web.request.AuditingVoucherRequest;
import org.finance.business.web.request.BatchDeleteVoucherRequest;
import org.finance.business.web.request.QuerySummaryVoucherRequest;
import org.finance.business.web.request.QueryVoucherBookRequest;
import org.finance.business.web.request.QueryVoucherItemCueRequest;
import org.finance.business.web.request.QueryVoucherRequest;
import org.finance.business.web.request.UnAuditingVoucherRequest;
import org.finance.business.web.request.UpdateVoucherRequest;
import org.finance.business.web.vo.CurrentPeriodOutlineOfVoucherVO;
import org.finance.business.web.vo.VoucherBookVO;
import org.finance.business.web.vo.VoucherDetailVO;
import org.finance.business.web.vo.VoucherItemVO;
import org.finance.business.web.vo.VoucherPrintContentVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.exception.HxException;
import org.finance.infrastructure.util.AssertUtil;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
    @Resource
    private AccountCloseListService accountCloseListService;
    @Resource
    private InitialBalanceService initialBalanceService;
    @Resource
    private SubjectService subjectService;
    private final static DateTimeFormatter yyyyMMFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final String RESOURCE_TARGET = "voucher";

    @GetMapping("/currentPeriodOutline")
    public R<CurrentPeriodOutlineOfVoucherVO> currentPeriodOutlineOfVoucher() {
        Integer currentYearMonth = SecurityUtil.getProxyCustomer().getCurrentPeriod();
        return R.ok(new CurrentPeriodOutlineOfVoucherVO()
                .setYearMonthNum(currentYearMonth)
                .setVoucherTotal(baseService.count(Wrappers.<Voucher>lambdaQuery()
                        .eq(Voucher::getYearMonthNum, currentYearMonth)
                )));
    }

    @GetMapping("/bySubject")
    public R<List<VoucherItemVO>> voucherItemBySubject(@Valid QuerySummaryVoucherRequest request) {
        List<Long> subjectIds = subjectService.listTogetherChildrenIds(request.getSubjectId());
        List<VoucherItemVO> voucherItemVOs = itemService.list(Wrappers.<VoucherItem>lambdaQuery()
                .in(VoucherItem::getSubjectId, subjectIds)
                .eq(VoucherItem::getYearMonthNum, request.getYearMonthNum())
            ).stream().map(VoucherConvert.INSTANCE::toVoucherItem)
            .collect(Collectors.toList());
        return R.ok(voucherItemVOs);
    }

    @GetMapping("/page")
    public RPage<VoucherDetailVO> pageVoucher(QueryVoucherRequest request) {
        Integer startPeriod = request.getStartPeriod();
        Integer endPeriod = request.getEndPeriod();
        Customer proxyCustomer = SecurityUtil.getProxyCustomer();
        Integer currentPeriod = proxyCustomer.getCurrentPeriod();
        IPage<VoucherDetailVO> page = baseService.page(request.extractPage(), Wrappers.<Voucher>lambdaQuery()
                .eq(request.getSerialNumber() != null, Voucher::getSerialNumber, request.getSerialNumber())
                .between(startPeriod != null && endPeriod != null, Voucher::getYearMonthNum, request.getStartPeriod(), request.getEndPeriod())
                .eq(startPeriod == null || endPeriod == null, Voucher::getYearMonthNum, currentPeriod)
                .orderByAsc(Voucher::getSerialNumber)
        ).convert(voucher -> {
            voucher.loadItems(itemService);
            return VoucherConvert.INSTANCE.toVoucherDetailVO(voucher);
        });
        return RPage.build(page);
    }

    @GetMapping("/page/book")
    public RPage<VoucherBookVO> pageVoucherBook(QueryVoucherBookRequest request) {
        return RPage.build(baseService.pageVoucherBookVO(request.extractPage()));
    }

    @GetMapping("/{id}")
    public R<VoucherDetailVO> voucherDetail(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/first")
    public R<VoucherDetailVO> firstVoucherDetail(Integer period) {
        Voucher voucher = Optional.ofNullable(
                baseService.getFirstAndItems(
                        Optional.ofNullable(period).orElseGet(SecurityUtil::getProxyCustomerCurrentPeriod)
                )
        ).orElseThrow(() -> HxException.warn("没有首张凭证")).loadItems(itemService);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/last")
    public R<VoucherDetailVO> lastVoucherDetail(Integer period) {
        Voucher voucher = Optional.ofNullable(
            baseService.getLastAndItems(
                Optional.ofNullable(period).orElseGet(SecurityUtil::getProxyCustomerCurrentPeriod)
            )
        ).orElseThrow(() -> HxException.warn("没有末张凭证")).loadItems(itemService);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/{serialNumber}/next")
    public R<VoucherDetailVO> nextVoucherDetail(@PathVariable("serialNumber") long serialNumber, Integer period) {
        Voucher voucher = Optional.ofNullable(
            baseService.getNextAndItems(serialNumber,
                Optional.ofNullable(period).orElseGet(SecurityUtil::getProxyCustomerCurrentPeriod)
            )
        ).orElseThrow(() -> HxException.warn("没有下一张凭证")).loadItems(itemService);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/{serialNumber}/prev")
    public R<VoucherDetailVO> prevVoucherDetail(@PathVariable("serialNumber") long serialNumber, Integer period) {
        Voucher voucher = Optional.ofNullable(
                baseService.getPrevAndItems(serialNumber,
                        Optional.ofNullable(period).orElseGet(SecurityUtil::getProxyCustomerCurrentPeriod)
                )
        ).orElseThrow(() -> HxException.warn("没有上一张凭证")).loadItems(itemService);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @GetMapping("/{id}/printContent")
    //@PreAuthorize("hasPermission('voucher', 'print')")
    public R<VoucherPrintContentVO> printContentOfVoucher(@PathVariable("id") long id) {
        Voucher voucher = baseService.getAndItemsById(id);
        VoucherPrintContentVO voucherPrintContentVO = VoucherConvert.INSTANCE.toVoucherPrintContentVO(voucher);
        voucherPrintContentVO.setCustomerName(customerService.getCustomerNameById(voucher.getCustomerId()));

        Function<Long, String> nameBySubjectId = subjectService.getNameFunction();
        voucherPrintContentVO.getItems().forEach(item -> {
            item.setSubjectName(nameBySubjectId.apply(item.getSubjectId()));
        });
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

    @GetMapping("/usableSerialNumber")
    public R<Integer> usableSerialNumber() {
        return R.ok(baseService.getMaxSerialNumberByYearMonth(SecurityUtil.getProxyCustomer().getCurrentPeriod()));
    }

    @GetMapping("/defaultVoucherDate")
    public R<LocalDate> defaultVoucherDate() {
        LocalDate defaultVoucherDate;
        Integer currentPeriod = accountCloseListService.currentAccountCloseYearMonth();
        // 从关账列表取日期
        if (currentPeriod != null) {
            defaultVoucherDate = YearMonth.parse(currentPeriod.toString(), Constants.YEAR_MONTH_FMT).atEndOfMonth();
        } else {
            // 从凭证列表取日期
            Voucher lastVoucher = baseService.getOne(Wrappers.<Voucher>lambdaQuery()
                    .orderByDesc(Voucher::getVoucherDate)
                    .last("limit 1")
            );
            defaultVoucherDate = Optional.ofNullable(lastVoucher)
                    .map(Voucher::getVoucherDate)
                    .orElseGet(() -> {
                        // 从初始余额取日期
                        InitialBalance initialBalance = initialBalanceService.getOne(
                                Wrappers.<InitialBalance>lambdaQuery().last("limit 1")
                        );
                        if (initialBalance == null) {
                            return LocalDate.now().minusMonths(1);
                        }
                        return LocalDate.of(initialBalance.getYear(), initialBalance.getYearMonthNum() % 100, 1);
                    });
        }
        defaultVoucherDate.plusMonths(1);
        return R.ok(defaultVoucherDate);
    }

    @PostMapping("/add")
    //@PreAuthorize("hasPermission('voucher', 'base')")
    public R<VoucherDetailVO> addVoucher(@Valid @RequestBody AddVoucherRequest request) {
        assertUnCloseAccount(request.getVoucherDate());
        if (request.getSerialNumber() != null) {
            assertSerialNumberNotExists(request.getYearMonthNum(), request.getSerialNumber());
        }
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, null);
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(voucher));
    }

    @PutMapping("/auditing/{id}")
    //@PreAuthorize("hasPermission('voucher', 'auditing')")
    public R auditingVoucher(@PathVariable("id") long id) {
        baseService.auditingById(id);
        return R.ok();
    }

    @PutMapping("/unAuditing/{id}")
    //@PreAuthorize("hasPermission('voucher', 'unAuditing')")
    public R unAuditingVoucher(@PathVariable("id") long id) {
        baseService.unAuditingById(id);
        return R.ok();
    }

    @PutMapping("/auditing")
    //@PreAuthorize("hasPermission('voucher:batch', 'auditing')")
    public R batchAuditingVoucher(@Valid @RequestBody AuditingVoucherRequest request) {
        baseService.batchAuditingVoucher(request.getIds());
        return R.ok();
    }

    @PutMapping("/unAuditing")
    //@PreAuthorize("hasPermission('voucher:batch', 'unAuditing')")
    public R batchUnAuditingVoucher(@Valid @RequestBody UnAuditingVoucherRequest request) {
        baseService.batchUnAuditingVoucher(request.getIds());
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    //@PreAuthorize("hasPermission('voucher', 'base')")
    public R<VoucherDetailVO> deleteVoucher(@PathVariable("id") long id) {
        Voucher voucher = baseService.getById(id);
        AssertUtil.isTrue(voucher.getSource() == Voucher.Source.MANUAL, "凭证来源：费用报销单，禁止删除！");
        assertUnCloseAccount(voucher.getVoucherDate());
        assertUnAudited(id);
        Integer serialNumber = voucher.getSerialNumber();
        Integer period = voucher.getYearMonthNum();
        baseService.deleteById(id);
        Voucher currVoucher = Optional.ofNullable(baseService.getPrevAndItems(serialNumber, period))
                .orElse(baseService.getNextAndItems(serialNumber, period));
        if (currVoucher != null) {
            currVoucher.loadItems(itemService);
        }
        return R.ok(VoucherConvert.INSTANCE.toVoucherDetailVO(currVoucher));
    }

    @DeleteMapping("/delete")
    //@PreAuthorize("hasPermission('voucher', 'base')")
    public R batchDeleteVoucher(@Valid @RequestBody BatchDeleteVoucherRequest request) {
        List<Long> allowDeleteIds = new ArrayList<>();
        request.getIds().forEach(id -> {
            Voucher voucher = baseService.getById(id);
            if (voucher.getSource() == Voucher.Source.EXPENSE_BILL) {
                return;
            }
            try {
                assertUnCloseAccount(voucher.getVoucherDate());
                assertUnAudited(id);
                allowDeleteIds.add(id);
            } catch (Exception ignored) {
            }
        });
        baseService.batchDelete(allowDeleteIds);
        return R.ok();
    }

    @PutMapping("/update")
    //@PreAuthorize("hasPermission('voucher', 'base')")
    public R updateVoucher(@Valid @RequestBody UpdateVoucherRequest request) {
        assertUnCloseAccount(request.getVoucherDate());
        assertUnAudited(request.getId());
        if (request.getSerialNumber() != null) {
            Voucher dbVoucher = baseService.getById(request.getId());
            if (!dbVoucher.getSerialNumber().equals(request.getSerialNumber())) {
                assertSerialNumberNotExists(request.getYearMonthNum(), request.getSerialNumber());
            }
        }
        Voucher voucher = VoucherConvert.INSTANCE.toVoucher(request);
        baseService.addOrUpdate(voucher, () -> itemService.removeByIds(request.getDeletedItemIds()));
        return R.ok();
    }

    private void assertUnCloseAccount(LocalDate voucherDate) {
        Integer currentPeriod = SecurityUtil.getProxyCustomer().getCurrentPeriod();
        boolean existsCloseAccountRecord = voucherDate.getYear() * 100 + voucherDate.getMonthValue() != currentPeriod;
        AssertUtil.isFalse(existsCloseAccountRecord, String.format("操作失败，月份：%s已结账！", voucherDate.format(yyyyMMFormatter)));
    }

    private void assertUnAudited(long voucherId) {
        boolean unAudited = baseService.count(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getId, voucherId)
                .eq(Voucher::getAuditStatus, AuditStatus.TO_BE_AUDITED)
        ) > 0;
        AssertUtil.isTrue(unAudited, "操作失败，该记录已审核！");
    }

    private void assertSerialNumberNotExists(int yearMonthNum, int serialNumber) {
        boolean existsSerialNumber = baseService.count(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getYearMonthNum, yearMonthNum)
                .eq(Voucher::getSerialNumber, serialNumber)
        ) > 0;
        AssertUtil.isFalse(existsSerialNumber, String.format("凭证序号：%s，已存在！", serialNumber));
    }
}
