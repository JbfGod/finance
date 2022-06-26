package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.Resource;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class TreeResourceVO {

    private Long id;

    private String number;

    private String name;

    private String parentNumber;

    private Boolean hasLeaf;

    private Integer level;

    private Resource.Type type;

    private String url;

    private String permitCode;

    private Integer sortNum;

    private Boolean disabled;

    private List<TreeResourceVO> children;

}