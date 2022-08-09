package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.InitialBalanceConvert;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.InitialBalanceItem;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.service.InitialBalanceItemService;
import org.finance.business.service.InitialBalanceService;
import org.finance.business.web.request.AddInitialBalanceRequest;
import org.finance.business.web.request.AuditingInitialBalanceRequest;
import org.finance.business.web.request.QueryInitialBalanceRequest;
import org.finance.business.web.request.UpdateInitialBalanceRequest;
import org.finance.business.web.vo.InitialBalanceItemVO;
import org.finance.business.web.vo.InitialBalanceVO;
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

/**
 * <p>
 * 初始余额 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-08
 */
@RestController
@RequestMapping("/api/initialBalance")
public class InitialBalanceWeb {

    @Resource
    private InitialBalanceService baseService;
    @Resource
    private InitialBalanceItemService itemService;

    @GetMapping("/outline")
    public R<InitialBalanceVO> initialBalanceOutline() {
        InitialBalanceVO initialBalance = InitialBalanceConvert.INSTANCE.toInitialBalanceVO(
            baseService.getOne(
                Wrappers.<InitialBalance>lambdaQuery().orderByDesc(InitialBalance::getYearMonthNum)
            )
        );
        return R.ok(initialBalance);
    }

    @GetMapping("/page")
    public RPage<InitialBalanceItemVO> pageInitialBalanceItem(QueryInitialBalanceRequest request) {
        IPage<InitialBalanceItemVO> page = itemService.page(request.extractPage(),
                Wrappers.<InitialBalanceItem>lambdaQuery().orderByDesc(InitialBalanceItem::getYearMonthNum)
        ).convert(InitialBalanceConvert.INSTANCE::toInitialBalanceItemVO);
        return RPage.build(page);
    }

    @PostMapping("/add")
    @PreAuthorize("hasPermission('initialBalance', 'base')")
    public R addInitialBalanceItem(@Valid @RequestBody AddInitialBalanceRequest request) {
        baseService.addOrUpdateItem(InitialBalanceConvert.INSTANCE.toInitialBalanceItem(request));
        return R.ok();
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission('initialBalance', 'base')")
    public R updateInitialBalance(@Valid @RequestBody UpdateInitialBalanceRequest request) {
        assertNotAuditAndNoBookkeeping(request.getId());
        baseService.addOrUpdateItem(InitialBalanceConvert.INSTANCE.toInitialBalanceItem(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasPermission('initialBalance', 'base')")
    public R deleteInitialBalance(@PathVariable("id") long id) {
        assertNotAuditAndNoBookkeeping(id);
        itemService.removeById(id);
        return R.ok();
    }

    @PutMapping("/auditing")
    @PreAuthorize("hasPermission('initialBalance', 'auditing')")
    public R<InitialBalanceVO> auditingInitialBalance(@Valid AuditingInitialBalanceRequest request) {
        return R.ok(
            InitialBalanceConvert.INSTANCE.toInitialBalanceVO(
               baseService.auditingByYearMonth(request.getYearMonthDate())
            )
        );
    }

    @PutMapping("/unAuditing/{id}")
    @PreAuthorize("hasPermission('initialBalance', 'unAuditing')")
    public R unAuditingInitialBalance(@PathVariable("id") long id) {
        baseService.unAuditingById(id);
        return R.ok();
    }
    @PutMapping("/bookkeeping/{id}")
    @PreAuthorize("hasPermission('initialBalance', 'bookkeeping')")
    public R bookkeepingInitialBalance(@PathVariable("id") long id) {
        baseService.bookkeepingById(id);
        return R.ok();
    }

    @PutMapping("/unBookkeeping/{id}")
    @PreAuthorize("hasPermission('initialBalance', 'unBookkeeping')")
    public R unBookkeepingInitialBalance(@PathVariable("id") long id) {
        baseService.unBookkeepingById(id);
        return R.ok();
    }

    private void assertNotAuditAndNoBookkeeping(long id) {
        boolean existsBookkeeping = baseService.count(Wrappers.<InitialBalance>lambdaQuery()
                .eq(InitialBalance::getId, id)
                .eq(InitialBalance::getBookkeeping, true)
        ) > 0;
        AssertUtil.isFalse(existsBookkeeping, "操作失败，初始余额已经记账！");
        boolean existsAudited = baseService.count(Wrappers.<InitialBalance>lambdaQuery()
                .eq(InitialBalance::getId, id)
                .eq(InitialBalance::getAuditStatus, AuditStatus.AUDITED)
        ) > 0;
        AssertUtil.isFalse(existsAudited, "操作失败，初始余额已经审核！");
    }

}