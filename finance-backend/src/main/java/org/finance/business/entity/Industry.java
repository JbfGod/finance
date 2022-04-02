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
 * 行业分类表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
public class Industry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 行业编号
     */
    private String number;

    /**
     * 行业名称
     */
    private String name;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 父级编号
     */
    private String parentNumber;

    /**
     * 是否有叶子节点
     */
    private Boolean hasLeaf;

    /**
     * 节点深度
     */
    private Integer level;

    /**
     * 节点左值
     */
    private Integer leftValue;

    /**
     * 节点右值
     */
    private Integer rightValue;

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

    public Industry setId(Long id) {
        this.id = id;
        return this;
    }
    public String getNumber() {
        return number;
    }

    public Industry setNumber(String number) {
        this.number = number;
        return this;
    }
    public String getName() {
        return name;
    }

    public Industry setName(String name) {
        this.name = name;
        return this;
    }
    public Long getParentId() {
        return parentId;
    }

    public Industry setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }
    public String getParentNumber() {
        return parentNumber;
    }

    public Industry setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
        return this;
    }
    public Boolean getHasLeaf() {
        return hasLeaf;
    }

    public Industry setHasLeaf(Boolean hasLeaf) {
        this.hasLeaf = hasLeaf;
        return this;
    }
    public Integer getLevel() {
        return level;
    }

    public Industry setLevel(Integer level) {
        this.level = level;
        return this;
    }
    public Integer getLeftValue() {
        return leftValue;
    }

    public Industry setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
        return this;
    }
    public Integer getRightValue() {
        return rightValue;
    }

    public Industry setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
        return this;
    }
    public String getCreateBy() {
        return createBy;
    }

    public Industry setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Industry setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }
    public String getModifyBy() {
        return modifyBy;
    }

    public Industry setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
        return this;
    }
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public Industry setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
    public Boolean getDeleted() {
        return deleted;
    }

    public Industry setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    public String toString() {
        return "Industry{" +
            "id=" + id +
            ", number=" + number +
            ", name=" + name +
            ", parentId=" + parentId +
            ", parentNumber=" + parentNumber +
            ", hasLeaf=" + hasLeaf +
            ", level=" + level +
            ", leftValue=" + leftValue +
            ", rightValue=" + rightValue +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", modifyBy=" + modifyBy +
            ", modifyTime=" + modifyTime +
            ", deleted=" + deleted +
        "}";
    }
}
