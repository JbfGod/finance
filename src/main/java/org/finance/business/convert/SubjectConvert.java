package org.finance.business.convert;

import org.finance.business.entity.Subject;
import org.finance.business.web.request.AddSubjectRequest;
import org.finance.business.web.request.UpdateSubjectInitialBalanceRequest;
import org.finance.business.web.request.UpdateSubjectRequest;
import org.finance.business.web.vo.SubjectVO;
import org.finance.business.web.vo.TreeSubjectVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface SubjectConvert {

    SubjectConvert INSTANCE = Mappers.getMapper( SubjectConvert.class );

    Subject clone(Subject subject);

    Subject toSubject(AddSubjectRequest request);

    Subject toSubject(UpdateSubjectRequest request);

    Subject toSubject(UpdateSubjectInitialBalanceRequest request);

    TreeSubjectVO toTreeSubjectVO(Subject subject);

    SubjectVO toSubjectVO(Subject subject);

    default List<TreeSubjectVO> toTreeSubjectVO(List<Subject> subjects, Consumer<TreeSubjectVO> enhanceCall) {
        List<TreeSubjectVO> treeSubjects = subjects.stream()
                .map(sub -> {
                    TreeSubjectVO treeSubjectVO = this.toTreeSubjectVO(sub);
                    enhanceCall.accept(treeSubjectVO);
                    return treeSubjectVO;
                })
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeSubjects, TreeSubjectVO::getId, TreeSubjectVO::getParentId
                , TreeSubjectVO::getChildren, TreeSubjectVO::setChildren);
    }

    default List<Subject> sumInitialBalance(List<Subject> subjects) {
        if (subjects.isEmpty()) {
            return subjects;
        }
        Integer maxLevel = subjects.stream().max(Comparator.comparingInt(Subject::getLevel))
                .get().getLevel();
        Map<Integer, List<Subject>> subjectsByLevel = subjects.stream().collect(Collectors.groupingBy(Subject::getLevel));
        for (int i = maxLevel - 1; i >= 1; i--) {
            List<Subject> subjectByLevel = subjectsByLevel.get(i);
            if (subjectByLevel == null) {
                continue;
            }
            for (Subject subject : subjectByLevel) {
                List<Subject> childrenSubject = subjectsByLevel.get(i + 1).stream()
                        .filter(child -> subject.getId().equals(child.getParentId()))
                        .collect(Collectors.toList());
                if (childrenSubject.isEmpty()) {
                    continue;
                }
                for (Subject child : childrenSubject) {
                    subject.setOpeningBalance(child.getOpeningBalance().add(subject.getOpeningBalance()));
                    subject.setDebitAnnualAmount(child.getDebitAnnualAmount().add(subject.getDebitAnnualAmount()));
                    subject.setCreditAnnualAmount(child.getCreditAnnualAmount().add(subject.getCreditAnnualAmount()));
                    subject.setBeginningBalance(child.getBeginningBalance().add(subject.getBeginningBalance()));
                }
            }
        }
        return subjects;
    }

}
