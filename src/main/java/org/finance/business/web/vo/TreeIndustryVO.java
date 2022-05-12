package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class TreeIndustryVO extends IndustryVO {

    private List<TreeIndustryVO> children;

}