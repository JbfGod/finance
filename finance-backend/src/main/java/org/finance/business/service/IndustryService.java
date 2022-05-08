package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Industry;
import org.finance.business.mapper.IndustryMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 行业分类表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class IndustryService extends ServiceImpl<IndustryMapper, Industry> {

    @Transactional(rollbackFor = Exception.class)
    public void add(Industry industry) {
        Long parentId = industry.getParentId();
        if (parentId == 0) {
            industry.setLevel(1);
            industry.setHasLeaf(false);
            industry.setParentNumber("0");
            industry.setLeftValue(1);
            industry.setRightValue(2);
            industry.setRootId(0L);
            baseMapper.insert(industry);
            baseMapper.updateById(industry.setRootId(industry.getId()));
            return;
        }
        Industry parentIndustry = baseMapper.selectById(parentId);
        Integer pRightValue = parentIndustry.getRightValue();
        Long rootId = parentIndustry.getRootId();

        if (!parentIndustry.getHasLeaf()) {
            this.update(Wrappers.<Industry>lambdaUpdate()
                    .set(Industry::getHasLeaf, true)
                    .eq(Industry::getId, parentId)
            );
        }

        industry.setHasLeaf(false);
        industry.setParentNumber(parentIndustry.getNumber());
        industry.setLevel(parentIndustry.getLevel() + 1);
        industry.setLeftValue(pRightValue);
        industry.setRightValue(pRightValue + 1);
        industry.setRootId(rootId);
        this.update(Wrappers.<Industry>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(Industry::getRootId, rootId)
                .ge(Industry::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<Industry>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(Industry::getRootId, rootId)
                .ge(Industry::getRightValue, pRightValue)
        );
        baseMapper.insert(industry);
    }

    public void delete(long id) {
        Industry dbIndustry = baseMapper.selectById(id);
        AssertUtil.isTrue(dbIndustry != null, "行业不存在，删除失败！");
        baseMapper.delete(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getRootId, dbIndustry.getRootId())
                .ge(Industry::getLeftValue, dbIndustry.getLeftValue())
                .le(Industry::getRightValue, dbIndustry.getRightValue())
        );
    }
    
}
