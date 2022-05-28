package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.convert.ExpenseBillConvert;
import org.finance.business.entity.ExpenseBill;
import org.finance.business.entity.ExpenseItem;
import org.finance.business.entity.ExpenseItemAttachment;
import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.entity.User;
import org.finance.business.service.ExpenseBillService;
import org.finance.business.service.ExpenseItemAttachmentService;
import org.finance.business.service.ExpenseItemService;
import org.finance.business.service.ExpenseItemSubsidyService;
import org.finance.business.web.request.AddExpenseBillRequest;
import org.finance.business.web.request.QueryExpenseBillCueRequest;
import org.finance.business.web.request.QueryExpenseBillRequest;
import org.finance.business.web.request.QueryExpenseItemCueRequest;
import org.finance.business.web.request.UpdateExpenseBillRequest;
import org.finance.business.web.vo.ExpenseBillPrintContentVO;
import org.finance.business.web.vo.ExpenseBillDetailVO;
import org.finance.business.web.vo.ExpenseBillVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.SnowflakeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 费用报销单 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-14
 */
@RestController
@RequestMapping("/api/expense/bill")
public class ExpenseBillWeb {

    @Resource
    private ExpenseBillService baseService;
    @Resource
    private ExpenseItemService itemService;
    @Resource
    private ExpenseItemSubsidyService subsidyService;
    @Resource
    private ExpenseItemAttachmentService attachmentService;
    private final String AUTH_BILL_SEARCH_ALL = "expense:bill:searchAll";

    @GetMapping("/page")
    public RPage<ExpenseBillVO> pageExpenseBill(QueryExpenseBillRequest request) {
        boolean canSearchAll = SecurityUtil.hasAuthority(AUTH_BILL_SEARCH_ALL);
        User currentUser = SecurityUtil.getCurrentUser();
        IPage<ExpenseBillVO> page = baseService.page(request.extractPage(), Wrappers.<ExpenseBill>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(request.getNumber()), ExpenseBill::getNumber, request.getNumber())
                .eq(!canSearchAll, ExpenseBill::getCreateBy, currentUser.getId())
        ).convert(ExpenseBillConvert.INSTANCE::toExpenseBillVO);
        return RPage.build(page);
    }

    @GetMapping("/get/{id}")
    public R<ExpenseBillDetailVO> expenseBillById(@PathVariable("id") long id) {
        ExpenseBill bill = baseService.getById(id);
        List<ExpenseItem> items = itemService.list(Wrappers.<ExpenseItem>lambdaQuery().eq(ExpenseItem::getBillId, id));
        bill.setItems(items);

        items.forEach(item -> {
            item.setSubsidies(subsidyService.list(Wrappers.<ExpenseItemSubsidy>lambdaQuery()
                    .eq(ExpenseItemSubsidy::getBillId, item.getBillId())
                    .eq(ExpenseItemSubsidy::getItemId, item.getId())
            ));
            item.setAttachments(attachmentService.list(Wrappers.<ExpenseItemAttachment>lambdaQuery()
                    .eq(ExpenseItemAttachment::getBillId, item.getBillId())
                    .eq(ExpenseItemAttachment::getItemId, item.getId())
            ));
        });
        return R.ok(ExpenseBillConvert.INSTANCE.toExpenseBillDetailVO(bill));
    }

    @GetMapping("/{id}/printContent")
    public R<ExpenseBillPrintContentVO> printContentOfExpenseBill(@PathVariable("id") long id) {
        ExpenseBill bill = baseService.getById(id);
        List<ExpenseItem> items = itemService.list(Wrappers.<ExpenseItem>lambdaQuery().eq(ExpenseItem::getBillId, id));
        bill.setItems(items);
        return R.ok(ExpenseBillConvert.INSTANCE.toExpenseBillPreviewVO(bill));
    }

    @GetMapping("/searchBillCue")
    public R<List<String>> searchExpenseBillCue(QueryExpenseBillCueRequest request) {
        boolean canSearchAll = SecurityUtil.hasAuthority(AUTH_BILL_SEARCH_ALL);
        User currentUser = SecurityUtil.getCurrentUser();
        QueryExpenseBillCueRequest.Column column = request.getColumn();
        List<String> cues = baseService.list(Wrappers.<ExpenseBill>lambdaQuery()
                .select(column.getColumnFunc())
                .eq(!canSearchAll, ExpenseBill::getCreateBy, currentUser.getId())
                .likeRight(column.getColumnFunc(), request.getKeyword())
                .groupBy(column.getColumnFunc(), ExpenseBill::getModifyTime)
                .orderByDesc(column.getColumnFunc(), ExpenseBill::getModifyTime)
                .last(String.format("limit %d", Optional.ofNullable(request.getNum()).orElse(5)))
        ).stream().map(column.getColumnFunc()).distinct().collect(Collectors.toList());
        return R.ok(cues);
    }

    @GetMapping("/searchItemCue")
    public R<List<String>> searchExpenseItemCue(QueryExpenseItemCueRequest request) {
        boolean canSearchAll = SecurityUtil.hasAuthority(AUTH_BILL_SEARCH_ALL);
        User currentUser = SecurityUtil.getCurrentUser();
        QueryExpenseItemCueRequest.Column column = request.getColumn();
        List<String> cues = itemService.list(Wrappers.<ExpenseItem>lambdaQuery()
                .select(column.getColumnFunc())
                .eq(!canSearchAll, ExpenseItem::getCreateBy, currentUser.getId())
                .likeRight(column.getColumnFunc(), request.getKeyword())
                .groupBy(column.getColumnFunc(), ExpenseItem::getModifyTime)
                .orderByDesc(column.getColumnFunc(), ExpenseItem::getModifyTime)
                .last(String.format("limit %d", Optional.ofNullable(request.getNum()).orElse(5)))
        ).stream().map(column.getColumnFunc()).distinct().collect(Collectors.toList());
        return R.ok(cues);
    }

    @GetMapping("/getBillNumber")
    public R<String> getBillNumber() {
        return R.ok(SnowflakeUtil.nextIdStr());
    }

    @PostMapping("/add")
    public R addExpenseBill(@Valid AddExpenseBillRequest request) {
        ExpenseBill expenseBill = ExpenseBillConvert.INSTANCE.toExpenseBill(request);
        baseService.addOrUpdate(expenseBill, null);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateExpenseBill(@Valid UpdateExpenseBillRequest request) {
        ExpenseBill expenseBill = ExpenseBillConvert.INSTANCE.toExpenseBill(request);
        baseService.addOrUpdate(expenseBill, () -> {
            itemService.deleteByIds(request.getDeletedItemIds());
            subsidyService.removeByIds(request.getDeletedSubsidyIds());
            attachmentService.removeByIds(request.getDeletedAttachmentIds());
        });
        return R.ok();
    }

}
