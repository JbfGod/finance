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
 * 功能表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
public class Function implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 功能编号
     */
    private String number;

    /**
     * 功能名称
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
     * 功能类型：MENU,BUTTON
     */
    private Type type;

    /**
     * 访问链接
     */
    private String url;

    /**
     * 权限代码
     */
    private String permitCode;

    /**
     * 排序编号升序
     */
    private Integer sortNum;

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

    public enum Type {
        MENU, BUTTON
    }

    public Long getId() {
        return id;
    }

    public Function setId(Long id) {
        this.id = id;
        return this;
    }
    public String getNumber() {
        return number;
    }

    public Function setNumber(String number) {
        this.number = number;
        return this;
    }
    public String getName() {
        return name;
    }

    public Function setName(String name) {
        this.name = name;
        return this;
    }
    public Long getParentId() {
        return parentId;
    }

    public Function setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }
    public String getParentNumber() {
        return parentNumber;
    }

    public Function setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
        return this;
    }
    public Boolean getHasLeaf() {
        return hasLeaf;
    }

    public Function setHasLeaf(Boolean hasLeaf) {
        this.hasLeaf = hasLeaf;
        return this;
    }
    public Integer getLevel() {
        return level;
    }

    public Function setLevel(Integer level) {
        this.level = level;
        return this;
    }
    public Type getType() {
        return type;
    }

    public Function setType(Type type) {
        this.type = type;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Function setUrl(String url) {
        this.url = url;
        return this;
    }
    public String getPermitCode() {
        return permitCode;
    }

    public Function setPermitCode(String permitCode) {
        this.permitCode = permitCode;
        return this;
    }
    public Integer getSortNum() {
        return sortNum;
    }

    public Function setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
        return this;
    }
    public String getCreateBy() {
        return createBy;
    }

    public Function setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Function setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }
    public String getModifyBy() {
        return modifyBy;
    }

    public Function setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
        return this;
    }
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public Function setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
    public Boolean getDeleted() {
        return deleted;
    }

    public Function setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    public String toString() {
        return "Function{" +
            "id=" + id +
            ", number=" + number +
            ", name=" + name +
            ", parentId=" + parentId +
            ", parentNumber=" + parentNumber +
            ", hasLeaf=" + hasLeaf +
            ", level=" + level +
            ", type=" + type +
            ", url=" + url +
            ", permitCode=" + permitCode +
            ", sortNum=" + sortNum +
            ", createBy=" + createBy +
            ", createTime=" + createTime +
            ", modifyBy=" + modifyBy +
            ", modifyTime=" + modifyTime +
            ", deleted=" + deleted +
        "}";
    }
}
