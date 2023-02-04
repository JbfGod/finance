package org.finance.business.entity.templates.accountting.system;

import org.finance.business.entity.Report;
import org.finance.business.entity.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public abstract class AbstractSystem {

    public List<Subject> subjects;
    public List<Report> reports;

    public void initial() {
        List<Subject> subjects = this.getSubjects();
        Map<String, Subject> subjectByNumber = subjects.stream().collect(Collectors.toMap(Subject::getNumber, v -> v));
        subjects.stream().peek(subject ->
                subject.setParentNumber(findParent(subject).map(Subject::getNumber).orElse("0"))
        ).forEach(subject -> {
            String parentNumber = subject.getParentNumber();
            Subject parent = subjectByNumber.get(parentNumber);
            if (parent == null) {
                subject.setLevel(1).setHasLeaf(false);
                return;
            }
            parent.setHasLeaf(true);
            Optional.ofNullable(parent.getChildren())
                    .orElseGet(() -> parent.setChildren(new ArrayList<>()).getChildren())
                    .add(subject.setLevel(parent.getLevel() + 1));
        });
    }

    private Optional<Subject> findParent(Subject subject) {
        List<Subject> subjects = this.getSubjects();
        return subjects.stream().filter(sub -> {
            if (sub == subject) {
                return false;
            }
            return subject.getNumber().startsWith(sub.getNumber());
        }).max(Comparator.comparing(sub -> sub.getNumber().length()));
    }

    public abstract List<Subject> getSubjects();

    public abstract List<Report> getReports();
}
