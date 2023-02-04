package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.VoucherItemMapper;
import org.finance.business.service.VoucherItemService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 凭证
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long customerId;

    /**
     * 客户编号
     */
    @TableField(fill = FieldFill.INSERT)
    private String customerNumber;

    /**
     * 凭证来源，MANUAL：手工，EXPENSE_BILL：费用报销单转
     */
    private Source source;

    /**
     * source=EXPENSE_BILL时>0
     */
    private Long expenseBillId;

    /**
     * 年份:yyyy
     */
    private Integer year;

    /**
     * 月份:yyyyMM
     */
    private Integer yearMonthNum;

    /**
     * 凭证日期
     */
    private LocalDate voucherDate;

    /**
     * 原币ID
     */
    private Long currencyId;

    /**
     * 凭证序号,每月凭证从1开始
     */
    private Integer serialNumber;

    /**
     * 附件张数
     */
    private Integer attachmentNum;

    /**
     * 借方原币合计金额
     */
    private BigDecimal totalDebitAmount;
    /**
     * 贷方原币合计金额
     */
    private BigDecimal totalCreditAmount;

    /**
     * 借方本币合计金额
     */
    private BigDecimal totalLocalDebitAmount;
    /**
     * 贷方本币合计金额
     */
    private BigDecimal totalLocalCreditAmount;

    /**
     * 审核状态
     */
    private AuditStatus auditStatus;

    /**
     * 审核人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long auditBy;

    /**
     * 审核人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String auditorName;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modifyBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private List<VoucherItem> items;

    /**
     * 凭证来源
     */
    public enum Source {
        /**
         * 手工
         */
        MANUAL,
        /**
         * 费用报销单
         */
        EXPENSE_BILL
    }

    public Voucher loadItems(VoucherItemMapper itemMapper) {
        List<VoucherItem> items = itemMapper.selectList(
                Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, this.id)
                    .orderByAsc(VoucherItem::getSerialNumber)
        );
        return this.setItems(items);
    }

    public Voucher loadItems(VoucherItemService itemService) {
        List<VoucherItem> items = itemService.list(
                Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getVoucherId, this.id)
                        .orderByAsc(VoucherItem::getSerialNumber)
        );
        return this.setItems(items);
    }
}
