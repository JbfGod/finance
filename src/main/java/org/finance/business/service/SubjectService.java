package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (parentId == 0) {
            subject.setHasLeaf(false);
            subject.setParentNumber("0");
            subject.setLevel(1);
            subject.setLeftValue(1);
            subject.setRightValue(2);
            subject.setRootNumber(subject.getNumber());
            baseMapper.insert(subject);
            return;
        }
        Subject parentSubject = baseMapper.selectById(parentId);
        Long industryId = parentSubject.getIndustryId();
        Integer pRightValue = parentSubject.getRightValue();
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
        subject.setLeftValue(pRightValue);
        subject.setRightValue(pRightValue + 1);
        subject.setRootNumber(rootNumber);
        this.update(Wrappers.<Subject>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(Subject::getCustomerId, parentSubject.getCustomerId())
                .eq(Subject::getRootNumber, rootNumber)
                .eq(Subject::getIndustryId, industryId)
                .ge(Subject::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<Subject>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(Subject::getCustomerId, parentSubject.getCustomerId())
                .eq(Subject::getRootNumber, rootNumber)
                .eq(Subject::getIndustryId, industryId)
                .ge(Subject::getRightValue, pRightValue)
        );
        baseMapper.insert(subject);
    }

    public void delete(long id) {
        Subject dbSubject = baseMapper.selectById(id);
        AssertUtil.isTrue(dbSubject != null, "行业不存在，删除失败！");
        baseMapper.delete(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getRootNumber, dbSubject.getRootNumber())
                .ge(Subject::getLeftValue, dbSubject.getLeftValue())
                .le(Subject::getRightValue, dbSubject.getRightValue())
        );
    }

    public boolean existsByIndustryId(long industryId) {
        return baseMapper.exists(Wrappers.<Subject>lambdaQuery().eq(Subject::getIndustryId, industryId));
    }
}