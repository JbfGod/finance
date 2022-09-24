package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Industry;
import org.finance.business.mapper.IndustryMapper;
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
            industry.setRootNumber(industry.getNumber());
            baseMapper.insert(industry);
            return;
        }
        Industry parentIndustry = baseMapper.selectById(parentId);
        Integer pRightValue = parentIndustry.getRightValue();
        String rootNumber = parentIndustry.getRootNumber();

        if (!parentIndustry.getHasLeaf()) {
            this.update(Wrappers.<Industry>lambdaUpdate()
                    .eq(Industry::getCustomerId, parentIndustry.getCustomerId())
                    .set(Industry::getHasLeaf, true)
                    .eq(Industry::getId, parentId)
            );
        }

        industry.setHasLeaf(false);
        industry.setParentNumber(parentIndustry.getNumber());
        industry.setLevel(parentIndustry.getLevel() + 1);
        industry.setLeftValue(pRightValue);
        industry.setRightValue(pRightValue + 1);
        industry.setRootNumber(rootNumber);
        this.update(Wrappers.<Industry>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(Industry::getCustomerId, parentIndustry.getCustomerId())
                .eq(Industry::getRootNumber, rootNumber)
                .ge(Industry::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<Industry>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(Industry::getCustomerId, parentIndustry.getCustomerId())
                .eq(Industry::getRootNumber, rootNumber)
                .ge(Industry::getRightValue, pRightValue)
        );
        baseMapper.insert(industry);
    }

    public void delete(long id) {
        Industry dbIndustry = baseMapper.selectById(id);
        AssertUtil.isTrue(dbIndustry != null, "行业不存在，删除失败！");
        baseMapper.delete(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getRootNumber, dbIndustry.getRootNumber())
                .ge(Industry::getLeftValue, dbIndustry.getLeftValue())
                .le(Industry::getRightValue, dbIndustry.getRightValue())
        );
    }

    public List<Long> listChildrenIdsById(long industryId) {
        Industry industry = baseMapper.selectById(industryId);
        return baseMapper.selectList(Wrappers.<Industry>lambdaQuery()
            .select(Industry::getId)
            .eq(Industry::getRootNumber, industry.getRootNumber())
            .ge(Industry::getLeftValue, industry.getLeftValue())
            .le(Industry::getRightValue, industry.getRightValue())
        ).stream().map(Industry::getId).collect(Collectors.toList());
    }

    public Function<Long, String> geNameFunction() {
        Map<Long, String> nameById = new HashMap<>(10);
        return (Long industryId) -> {
            if (nameById.containsKey(industryId)) {
                return nameById.get(industryId);
            }
            Industry industry = baseMapper.selectOne(Wrappers.<Industry>lambdaQuery()
                    .select(Industry::getName)
                    .eq(Industry::getId, industryId)
            );
            if (industry == null) {
                return "行业已被删除";
            }
            return industry.getName();
        };
    }

}
