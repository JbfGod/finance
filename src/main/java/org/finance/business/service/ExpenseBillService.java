package org.finance.business.service;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.ExpenseBill;
import org.finance.business.entity.ExpenseItem;
import org.finance.business.entity.ExpenseItemAttachment;
import org.finance.business.entity.ExpenseItemSubsidy;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.mapper.ExpenseBillMapper;
import org.finance.business.mapper.ExpenseItemAttachmentMapper;
import org.finance.business.mapper.ExpenseItemMapper;
import org.finance.business.mapper.ExpenseItemSubsidyMapper;
import org.finance.infrastructure.constants.BucketName;
import org.finance.infrastructure.exception.HxException;
import org.finance.infrastructure.util.AssertUtil;
import org.finance.infrastructure.util.SnowflakeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 费用报销单 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-14
 */
@Slf4j
@Service
public class ExpenseBillService extends ServiceImpl<ExpenseBillMapper, ExpenseBill> {

    @Resource
    private ExpenseItemMapper itemMapper;
    @Resource
    private ExpenseItemSubsidyMapper subsidyMapper;
    @Resource
    private ExpenseItemAttachmentMapper attachmentMapper;
    @Resource
    private MinioClient minioClient;

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        baseMapper.deleteById(id);
        itemMapper.delete(Wrappers.<ExpenseItem>lambdaQuery().eq(ExpenseItem::getBillId, id));
        subsidyMapper.delete(Wrappers.<ExpenseItemSubsidy>lambdaQuery().eq(ExpenseItemSubsidy::getBillId, id));
        attachmentMapper.delete(Wrappers.<ExpenseItemAttachment>lambdaQuery().eq(ExpenseItemAttachment::getBillId, id));
    }

    public void auditingById(long id) {
        boolean success = this.update(Wrappers.<ExpenseBill>lambdaUpdate()
                .set(ExpenseBill::getAuditStatus, AuditStatus.AUDITED)
                .eq(ExpenseBill::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(ExpenseBill::getId, id));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    public void unAuditingById(long id) {
        boolean success = this.update(Wrappers.<ExpenseBill>lambdaUpdate()
                .set(ExpenseBill::getAuditStatus, AuditStatus.TO_BE_AUDITED)
                .eq(ExpenseBill::getAuditStatus, AuditStatus.AUDITED)
                .eq(ExpenseBill::getId, id));
        AssertUtil.isTrue(success, "操作失败，该记录状态已发生变化！");
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(ExpenseBill bill, Runnable beforeCall) {
        if (beforeCall != null) {
            beforeCall.run();
        }
        this.addOrUpdateBill(bill);
        Long billId = bill.getId();

        List<ExpenseItem> items = bill.getItems();
        // 添加费用报销项
        for (int i = 0; i < items.size(); i++) {
            ExpenseItem item = items.get(i).setSerialNumber(i + 1).setBillId(billId);
            this.addOrUpdateItem(item);

            Long itemId = item.getId();
            // 添加补助费用明细
            List<ExpenseItemSubsidy> subsidies = Optional.ofNullable(item.getSubsidies()).orElse(Collections.emptyList());
            subsidies.forEach(subsidy -> {
                this.addOrUpdateSubsidy(subsidy.setBillId(billId).setItemId(itemId));
            });
            // 添加票据图片信息
            List<ExpenseItemAttachment> attachments = Optional.ofNullable(item.getAttachments()).orElse(Collections.emptyList());
            for (int j = 0; j < attachments.size(); j++) {
                ExpenseItemAttachment attachment = attachments.get(j)
                        .setBillId(billId).setItemId(itemId);
                addOrUpdateAttachment(attachment, j);
            }
        }
    }


    private void addOrUpdateBill(ExpenseBill bill) {
        if (bill.getId() != null) {
            baseMapper.updateById(bill);
            return;
        }
        if (StringUtils.isBlank(bill.getNumber())) {
            // 生成报销单号
            String billNumber = SnowflakeUtil.nextIdStr();
            bill.setNumber(billNumber);
        }
        boolean numberExists = baseMapper.exists(Wrappers.<ExpenseBill>lambdaQuery()
                .eq(ExpenseBill::getNumber, bill.getNumber())
        );
        AssertUtil.isFalse(numberExists, String.format("订单号：%s，已存在！", bill.getNumber()));

        // 报销日期不填，默认当前时间
        if (bill.getExpenseTime() == null) {
            bill.setExpenseTime(LocalDateTime.now());
        }
        baseMapper.insert(bill);
    }

    private void addOrUpdateItem(ExpenseItem item) {
        if (item.getId() != null) {
            itemMapper.updateById(item);
            return;
        }
        itemMapper.insert(item);
    }

    private void addOrUpdateSubsidy(ExpenseItemSubsidy subsidy) {
        if (subsidy.getId() != null) {
            subsidyMapper.updateById(subsidy);
            return;
        }
        subsidyMapper.insert(subsidy);
    }

    private void addOrUpdateAttachment(ExpenseItemAttachment attachment, int index) {
        if (attachment.getId() != null) {
            attachmentMapper.updateById(attachment);
            return;
        }
        String url = uploadBillFile(attachment, index);
        attachmentMapper.insert(attachment.setUrl(url));
    }

    private String uploadBillFile(ExpenseItemAttachment attachment, int index) {
        MultipartFile file = attachment.getFile();
        Long billId = attachment.getBillId();
        Long itemId = attachment.getItemId();
        String originalFilename = file.getOriginalFilename();
        String extName = FileUtil.extName(originalFilename);
        String objectName = String.format("%s-%s-%s.%s", billId, itemId, index, extName);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BucketName.EXPENSE_BILL.getName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("文件服务器繁忙，票据上传失败:", e);
            throw new HxException("文件服务器繁忙，票据上传失败！");
        }
        return String.format("/%s/%s", BucketName.EXPENSE_BILL.getName(), objectName);
    }
}
