package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CurrencyConvert;
import org.finance.business.entity.Currency;
import org.finance.business.service.CurrencyService;
import org.finance.business.web.request.AddCurrencyRequest;
import org.finance.business.web.request.QueryCurrencyRequest;
import org.finance.business.web.request.UpdateCurrencyRequest;
import org.finance.business.web.vo.CurrencyVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    private final DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");
    @Resource
    private CurrencyService baseService;

    @GetMapping("/listOfCurrentMonth")
    public R<List<CurrencyVO>> currencyOfCurrentMonth() {
        Integer yearMonthNum = Integer.parseInt(LocalDateTime.now().format(YEAR_MONTH_FMT));
        List<CurrencyVO> list = baseService.list(Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getYearMonthNum, yearMonthNum)
        ).stream().map(CurrencyConvert.INSTANCE::toCurrencyVO).collect(Collectors.toList());
        return R.ok(list);
    }

    @GetMapping("/get/{id}")
    public R<CurrencyVO> currencyById(@PathVariable("id") long id) {
        return R.ok(CurrencyConvert.INSTANCE.toCurrencyVO(baseService.getById(id)));
    }

    @GetMapping("/page")
    public RPage<CurrencyVO> pageCurrency(@Valid QueryCurrencyRequest request) {
        IPage<CurrencyVO> pages = baseService.page(request.extractPage(), Wrappers.<Currency>lambdaQuery()
                .eq(Currency::getYearMonthNum, request.getYearMonthNum())
                .orderByDesc(Currency::getModifyTime)
        ).convert(CurrencyConvert.INSTANCE::toCurrencyVO);
        return RPage.build(pages);
    }

    @PostMapping("/add")
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

    @PutMapping("/update")
    public R updateCurrency(@Valid @RequestBody UpdateCurrencyRequest request) {
        Currency currency = CurrencyConvert.INSTANCE.toCurrency(request);
        baseService.updateById(currency);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteCurrency(@PathVariable("id") long id) {
        baseService.removeById(id);
        return R.ok();
    }


}
