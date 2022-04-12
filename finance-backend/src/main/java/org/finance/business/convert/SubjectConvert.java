package org.finance.business.convert;

import org.finance.business.entity.Subject;
import org.finance.business.web.request.AddSubjectRequest;
import org.finance.business.web.request.UpdateSubjectRequest;
import org.finance.business.web.vo.SubjectVO;
import org.finance.business.web.vo.TreeSubjectVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface SubjectConvert {

    SubjectConvert INSTANCE = Mappers.getMapper( SubjectConvert.class );

    Subject toSubject(AddSubjectRequest request);

    Subject toSubject(UpdateSubjectRequest request);

    TreeSubjectVO toTreeSubjectVO(Subject category);

    SubjectVO toSubjectVO(Subject category);

    default List<TreeSubjectVO> toTreeSubjectVO(List<Subject> categories) {
        List<TreeSubjectVO> treeCategories = categories.stream().map(this::toTreeSubjectVO)
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeCategories, TreeSubjectVO::getId, TreeSubjectVO::getParentId
                , TreeSubjectVO::getChildren, TreeSubjectVO::setChildren);
    }
}
