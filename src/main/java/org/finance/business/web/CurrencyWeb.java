package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CurrencyConvert;
import org.finance.business.entity.Currency;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.service.CurrencyService;
import org.finance.business.web.request.AddCurrencyRequest;
import org.finance.business.web.request.CopyCurrencyRequest;
import org.finance.business.web.request.QueryCurrencyRequest;
import org.finance.business.web.request.UpdateCurrencyRequest;
import org.finance.business.web.vo.CurrencyVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 货币汇率列表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/api/currency")
public class CurrencyWeb {

    private final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");
    @Resource
    private CurrencyService baseService;

    @GetMapping("/listAuditedOfYearMonth")
    public R<List<CurrencyVO>> currencyOfYearMonth(Integer yearMonth) {
        yearMonth = Optional.ofNullable(yearMonth).orElseGet(() -> (
                Integer.valueOf(LocalDate.now().format(YEAR_MONTH_FMT))
        ));
        List<CurrencyVO> list = baseService.list(Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getAuditStatus, AuditStatus.AUDITED)
                .eq(Currency::getYearMonthNum, yearMonth)
        ).stream().map(CurrencyConvert.INSTANCE::toCurrencyVO).collect(Collectors.toList());
        return R.ok(list);
    }

    @GetMapping("/get/{id}")
    public R<CurrencyVO> currencyById(@PathVariable("id") long id) {
        return R.ok(CurrencyConvert.INSTANCE.toCurrencyVO(baseService.getById(id)));
    }

    @GetMapping("/page")
    public RPage<CurrencyVO> pageCurrency(QueryCurrencyRequest request) {
        IPage<CurrencyVO> pages = baseService.page(request.extractPage(), Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getYearMonthNum, request.getYearMonthNum())
                .orderByDesc(Currency::getModifyTime)
        ).convert(CurrencyConvert.INSTANCE::toCurrencyVO);
        return RPage.build(pages);
    }

    @PostMapping("/copy")
    @PreAuthorize("hasPermission('currency', 'operating')")
    public R copyCurrencyByMonth(@RequestBody CopyCurrencyRequest request) {
        baseService.copyCurrencyByMonth(request.getTargetYearMonth(), request.getSourceYearMonth(), request.isOverride());
        return R.ok();
    }

    @PostMapping("/add")
    @PreAuthorize("hasPermission('currency', 'operating')")
    public R addCurrency(@Valid @RequestBody AddCurrencyRequest request) {
        Currency currency = CurrencyConvert.INSTANCE.toCurrency(request);
        boolean existsNumber = baseService.count(Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getYearMonthNum, request.getYearMonthNum())
                .eq(Currency::getNumber, request.getNumber())
        ) > 0;
        AssertUtil.isFalse(existsNumber, "货币编号已存在！");
        baseService.save(currency);
        return R.ok();
    }

    @PutMapping("/auditing/{id}")
    @PreAuthorize("hasPermission('currency', 'auditing')")
    public R auditingCurrency(@PathVariable("id") long id) {
        baseService.auditingById(id);
        return R.ok();
    }

    @PutMapping("/unAuditing/{id}")
    @PreAuthorize("hasPermission('currency', 'unAuditing')")
    public R unAuditingCurrency(@PathVariable("id") long id) {
        baseService.unAuditingById(id);
        return R.ok();
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('currency', 'operating')")
    public R updateCurrency(@Valid @RequestBody UpdateCurrencyRequest request) {
        assertUnAudited(request.getId());
        Currency currency = CurrencyConvert.INSTANCE.toCurrency(request);
        baseService.updateById(currency);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('currency', 'operating')")
    public R deleteCurrency(@PathVariable("id") long id) {
        assertUnAudited(id);
        baseService.removeById(id);
        return R.ok();
    }

    private void assertUnAudited(long currencyId) {
        boolean unAudited = baseService.count(Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getId, currencyId)
                .eq(Currency::getAuditStatus, AuditStatus.AUDITED)
        ) > 0;
        AssertUtil.isTrue(unAudited, "操作失败，该记录已经审核！");
    }

}
