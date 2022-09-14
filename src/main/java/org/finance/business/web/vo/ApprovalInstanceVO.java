package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class ApprovalInstanceVO {

    private Integer currentLevel;
    private Boolean ended;
    private List<ApprovalInstanceItemVO> items;

}
