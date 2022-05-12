package org.finance.business.web.vo;

import lombok.Data;

/**
 * @author jiangbangfa
 */
@Data
public class CustomerCategoryVO {

    private Long id;

    private String number;

    private String name;

    private Boolean hasLeaf;

    private String parentNumber;

    private Long parentId;

    private String remark;
}
