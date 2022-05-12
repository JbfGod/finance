package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class TreeCustomerCategoryVO extends CustomerCategoryVO {

    private List<TreeCustomerCategoryVO> children;

}