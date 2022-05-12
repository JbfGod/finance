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
    /**
     * 用途：客户所属表标识
     */
    public final static String CATEGORY_CUSTOMER_TBL_ID = "CATEGORY_CUSTOMER_TBL_ID";

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
