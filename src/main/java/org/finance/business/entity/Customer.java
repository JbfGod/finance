package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Data
@Accessors(chain = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_NUMBER = "HX_TOP";
    public static final String DEFAULT_NAME = "记账平台";
    public static final Long DEFAULT_ID = 0L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号
     */
    private String number;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 业务负责人
     */
    private Long businessUserId;

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
    private Type type;

    /**
     * 客户是否启用
     */
    private Boolean enabled;

    /**
     * 客户状态
     */
    private Status status;

    /**
     * 租赁生效时间
     */
    private LocalDateTime effectTime;

    /**
     * 租赁过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
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

    public boolean isSuperCustomer() {
        return DEFAULT_NUMBER.equals(this.number);
    }

    public enum Status {
        /**
         * 初始化数据中
         */
        INITIALIZING,
        /**
         * 初始化完毕
         */
        SUCCESS,
        ;
    }

    public enum Type {
        /**
         * 租用
         */
        RENT,
        /**
         * 代理
         */
        PROXY,
        /**
         * 租用 + 代理
         */
        RENT_AND_PROXY
    }

}
