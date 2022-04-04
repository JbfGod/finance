package org.finance.business.web.vo;

import lombok.Data;
import org.finance.business.entity.Function;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class TreeFunctionVO {

    private String number;

    private String name;

    private String parentNumber;

    private Boolean hasLeaf;

    private Integer level;

    private Function.Type type;

    private String url;

    private String permitCode;

    private Integer sortNum;

    private List<TreeFunctionVO> children;

}
