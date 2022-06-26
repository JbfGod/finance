package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class TreeResourceWithOperateVO {

    private String id;
    private String name;
    private String parentId;
    private Integer sortNum;
    private Boolean disabled;

    private List<TreeResourceWithOperateVO> children;

}