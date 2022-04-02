package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 登录账户
     */
    private Long userId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 所属行业
     */
    private Long industryId;

    /**
     * 客户类别
     */
    private Long categoryId;

    /**
     * 客户类型：租用、代理、租用+代理
     */
    private String type;

    /**
     * 客户状态：停用、启用
     */
    private String status;

    /**
     * 租赁生效时间
     */
    private LocalDateTime effectTime;

    /**
     * 租赁过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 客户电话
     */
    private String telephone;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 银行开户名称
     */
    private String bankAccountName;

    /**
     * 是否使用外汇
     */
    private Boolean useForeignExchange;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    @TableLogic
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public Customer setId(Long id) {
        this.id = id;
        return this;
    }
    public Long getUserId() {
        return userId;
    }

    public Customer setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }
    public Long getIndustryId() {
        return industryId;
    }

    public Customer setIndustryId(Long industryId) {
        this.industryId = industryId;
        return this;
    }
    public Long getCategoryId() {
        return categoryId;
    }

    public Customer setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    public String getType() {
        return type;
    }

    public Customer setType(String type) {
        this.type = type;
        return this;
    }
    public String getStatus() {
        return status;
    }

    public Customer setStatus(String status) {
        this.status = status;
        return this;
    }
    public LocalDateTime getEffectTime() {
        return effectTime;
    }

    public Customer setEffectTime(LocalDateTime effectTime) {
        this.effectTime = effectTime;
        return this;
    }
    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public Customer setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
        return this;
    }
    public String getTelephone() {
        return telephone;
    }

    public Customer setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }
    public String getBankAccount() {
        return bankAccount;
    }

    public Customer setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }
    public String getBankAccountName() {
        return bankAccountName;
    }

    public Customer setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
        return this;
    }
    public Boolean getUseForeignExchange() {
        return useForeignExchange;
    }

    public Customer setUseForeignExchange(Boolean useForeignExchange) {
        this.useForeignExchange = useForeignExchange;
        return this;
    }
    public String getRemark() {
        return remark;
    }

    public Customer setRemark(String remark) {
        this.remark = remark;
        return this;
    }
    public String getCreateBy() {
        return createBy;
    }

    public Customer setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Customer setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }
    public String getModifyBy() {
        return modifyBy;
    }

    public Customer setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
        return this;
    }
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public Customer setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
    public Boolean getDeleted() {
        return deleted;
    }

    public Customer setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", userId=" + userId +
            ", name=" + name +
            ", industryId=" + industryId +
            ", categoryId=" + categoryId +
            ", type=" + type +
            ", status=" + status +
            ", effectTime=" + effectTime +
            ", expireTime=" + expireTime +
            ", telephone=" + telephone +
            ", bankAccount=" + bankAccount +
            ", bankAccountName=" + bankAccountName +
            ", useForeignExchange=" + useForeignExchange +
            ", remark=" + remark +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", modifyBy=" + modifyBy +
            ", modifyTime=" + modifyTime +
            ", deleted=" + deleted +
        "}";
    }
}
