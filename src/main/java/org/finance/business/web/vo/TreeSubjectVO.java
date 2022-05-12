package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class TreeSubjectVO extends SubjectVO {

    private List<TreeSubjectVO> children;

}