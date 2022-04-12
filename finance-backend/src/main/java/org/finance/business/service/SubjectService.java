package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Sequence;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.SequenceMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Resource
    private SequenceMapper sequenceMapper;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void add(Subject subject) {
        Long parentId = subject.getParentId();
        if (parentId == 0) {
            Sequence sequence = new Sequence().setUseCategory(Sequence.TREE_ROOT_ID_OF_INDUSTRY);
            sequenceMapper.insert(sequence);
            subject.setHasLeaf(false);
            subject.setLevel(1);
            subject.setParentNumber("0");
            subject.setLeftValue(1);
            subject.setRightValue(2);
            subject.setRootId(sequence.getId());
            baseMapper.insert(subject);
            return;
        }
        Subject parentSubject = baseMapper.selectById(parentId);
        Long industryId = parentSubject.getIndustryId();
        Integer pRightValue = parentSubject.getRightValue();
        Long rootId = parentSubject.getRootId();

        subject.setHasLeaf(false);
        subject.setParentNumber(parentSubject.getNumber());
        subject.setLevel(parentSubject.getLevel() + 1);
        subject.setLeftValue(pRightValue);
        subject.setRightValue(pRightValue + 1);
        subject.setRootId(rootId);
        this.update(Wrappers.<Subject>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(Subject::getRootId, rootId)
                .eq(Subject::getIndustryId, industryId)
                .ge(Subject::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<Subject>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(Subject::getRootId, rootId)
                .eq(Subject::getIndustryId, industryId)
                .ge(Subject::getRightValue, pRightValue)
        );
        baseMapper.insert(subject);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void delete(long id) {
        Subject dbSubject = baseMapper.selectById(id);
        AssertUtil.isTrue(dbSubject != null, "行业不存在，删除失败！");
        baseMapper.delete(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getRootId, dbSubject.getRootId())
                .ge(Subject::getLeftValue, dbSubject.getLeftValue())
                .le(Subject::getRightValue, dbSubject.getRightValue())
        );
    }
    
}
