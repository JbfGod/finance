package org.finance.business.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.business.entity.enums.ResourceModule;

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
    private ResourceModule module;

    private List<TreeResourceWithOperateVO> children;

}