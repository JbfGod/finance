package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.CustomerCategory;
import org.finance.business.mapper.CustomerCategoryMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户分类表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class CustomerCategoryService extends ServiceImpl<CustomerCategoryMapper, CustomerCategory> {

    @Transactional(rollbackFor = Exception.class)
    public void add(CustomerCategory customerCategory) {
        Long parentId = customerCategory.getParentId();
        if (parentId == 0) {
            customerCategory.setHasLeaf(false);
            customerCategory.setLevel(1);
            customerCategory.setParentNumber("0");
            customerCategory.setLeftValue(1);
            customerCategory.setRightValue(2);
            customerCategory.setRootNumber(customerCategory.getNumber());
            baseMapper.insert(customerCategory);
            return;
        }
        CustomerCategory parentCategory = baseMapper.selectById(parentId);
        Integer pRightValue = parentCategory.getRightValue();
        String rootNumber = parentCategory.getRootNumber();

        if (!parentCategory.getHasLeaf()) {
            this.update(Wrappers.<CustomerCategory>lambdaUpdate()
                    .set(CustomerCategory::getHasLeaf, true)
                    .eq(CustomerCategory::getId, parentId)
            );
        }

        customerCategory.setHasLeaf(false);
        customerCategory.setParentNumber(parentCategory.getNumber());
        customerCategory.setLevel(parentCategory.getLevel() + 1);
        customerCategory.setLeftValue(pRightValue);
        customerCategory.setRightValue(pRightValue + 1);
        customerCategory.setRootNumber(rootNumber);
        this.update(Wrappers.<CustomerCategory>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(CustomerCategory::getRootNumber, rootNumber)
                .ge(CustomerCategory::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<CustomerCategory>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(CustomerCategory::getRootNumber, rootNumber)
                .ge(CustomerCategory::getRightValue, pRightValue)
        );
        baseMapper.insert(customerCategory);
    }

    public void delete(long id) {
        CustomerCategory dbCategory = baseMapper.selectById(id);
        AssertUtil.isTrue(dbCategory != null, "客户类别不存在，删除失败！");
        baseMapper.delete(Wrappers.<CustomerCategory>lambdaQuery()
                .eq(CustomerCategory::getRootNumber, dbCategory.getRootNumber())
                .ge(CustomerCategory::getLeftValue, dbCategory.getLeftValue())
                .le(CustomerCategory::getRightValue, dbCategory.getRightValue())
        );
    }

    public List<Long> listChildrenIdsById(Long categoryId) {
        CustomerCategory category = baseMapper.selectById(categoryId);
        return baseMapper.selectList(Wrappers.<CustomerCategory>lambdaQuery()
                .select(CustomerCategory::getId)
                .eq(CustomerCategory::getRootNumber, category.getRootNumber())
                .ge(CustomerCategory::getLeftValue, category.getLeftValue())
                .le(CustomerCategory::getRightValue, category.getRightValue())
        ).stream().map(CustomerCategory::getId).collect(Collectors.toList());
    }

}
