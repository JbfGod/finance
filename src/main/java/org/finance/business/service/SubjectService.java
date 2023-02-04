package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 科目表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class SubjectService extends ServiceImpl<SubjectMapper, Subject> {

    @Transactional(rollbackFor = Exception.class)
    public void add(Subject subject) {
        Long parentId = subject.getParentId();
        if (parentId == null || parentId == 0) {
            subject.setHasLeaf(false);
            subject.setParentNumber("0");
            subject.setLevel(1);
            subject.setRootNumber(subject.getNumber());
            baseMapper.insert(subject);
            return;
        }
        Subject parentSubject = baseMapper.selectById(parentId);
        String rootNumber = parentSubject.getRootNumber();

        if (!parentSubject.getHasLeaf()) {
            this.update(Wrappers.<Subject>lambdaUpdate()
                    .eq(Subject::getCustomerId, parentSubject.getCustomerId())
                    .set(Subject::getHasLeaf, true)
                    .eq(Subject::getId, parentId)
            );
        }

        subject.setHasLeaf(false);
        subject.setParentNumber(parentSubject.getNumber());
        subject.setLevel(parentSubject.getLevel() + 1);
        subject.setRootNumber(rootNumber);
        baseMapper.insert(subject);
    }

    public void delete(long id) {
        Subject dbSubject = baseMapper.selectById(id);
        AssertUtil.isTrue(dbSubject != null, "科目不存在，删除失败！");
        AssertUtil.isFalse(baseMapper.exists(Wrappers.<Subject>lambdaQuery().eq(Subject::getParentId, id)), "存在下级科目，删除失败！");
        baseMapper.deleteById(id);
    }

    public Function<Long, String> getNameFunction() {
        Map<Long, String> nameById = new HashMap<>(10);
        return (Long userId) -> {
            if (nameById.containsKey(userId)) {
                return nameById.get(userId);
            }
            Subject subject = baseMapper.selectOne(Wrappers.<Subject>lambdaQuery()
                    .select(Subject::getName)
                    .eq(Subject::getId, userId)
            );
            return subject == null ? "无效的科目" : subject.getName();
        };
    }

    public List<Long> listTogetherChildrenIds(long subjectId) {
        Subject subject = baseMapper.selectById(subjectId);
        return baseMapper.selectList(Wrappers.<Subject>lambdaQuery()
            .select(Subject::getId).likeRight(Subject::getNumber, subject.getNumber())
        ).stream().map(Subject::getId).collect(Collectors.toList());
    }

    public List<Subject> listChildren(long subjectId) {
        return baseMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getParentId, subjectId));
    }
}
