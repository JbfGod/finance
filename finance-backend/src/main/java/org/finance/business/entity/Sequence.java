package org.finance.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 序列表
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Data
@Accessors(chain = true)
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static String CATEGORY_CUSTOMER = "CUSTOMER";
    public final static String TREE_ROOT_ID = "TREE_ROOT_ID";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 使用类别
     */
    private String useCategory;

}
