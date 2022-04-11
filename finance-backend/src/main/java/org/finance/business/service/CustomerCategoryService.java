package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.CustomerCategory;
import org.finance.business.entity.Sequence;
import org.finance.business.mapper.CustomerCategoryMapper;
import org.finance.business.mapper.SequenceMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Resource
    private SequenceMapper sequenceMapper;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void add(CustomerCategory customerCategory) {
        Long parentId = customerCategory.getParentId();
        if (parentId == 0) {
            Sequence sequence = new Sequence().setUseCategory(Sequence.TREE_ROOT_ID_OF_CUSTOMER_CATEGORY);
            sequenceMapper.insert(sequence);
            customerCategory.setHasLeaf(false);
            customerCategory.setLevel(1);
            customerCategory.setParentNumber("0");
            customerCategory.setLeftValue(1);
            customerCategory.setRightValue(2);
            customerCategory.setRootId(sequence.getId());
            baseMapper.insert(customerCategory);
            return;
        }
        CustomerCategory parentCategory = baseMapper.selectById(parentId);
        Integer pRightValue = parentCategory.getRightValue();
        Long rootId = parentCategory.getRootId();

        customerCategory.setHasLeaf(false);
        customerCategory.setParentNumber(parentCategory.getNumber());
        customerCategory.setLevel(parentCategory.getLevel() + 1);
        customerCategory.setLeftValue(pRightValue);
        customerCategory.setRightValue(pRightValue + 1);
        customerCategory.setRootId(rootId);
        this.update(Wrappers.<CustomerCategory>lambdaUpdate()
                .setSql("left_value = left_value + 2")
                .eq(CustomerCategory::getRootId, rootId)
                .ge(CustomerCategory::getLeftValue, pRightValue)
        );
        this.update(Wrappers.<CustomerCategory>lambdaUpdate()
                .setSql("right_value = right_value + 2")
                .eq(CustomerCategory::getRootId, rootId)
                .ge(CustomerCategory::getRightValue, pRightValue)
        );
        baseMapper.insert(customerCategory);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void delete(long id) {
        CustomerCategory dbCategory = baseMapper.selectById(id);
        AssertUtil.isTrue(dbCategory != null, "客户类别不存在，删除失败！");
        baseMapper.delete(Wrappers.<CustomerCategory>lambdaQuery()
                .eq(CustomerCategory::getRootId, dbCategory.getRootId())
                .ge(CustomerCategory::getLeftValue, dbCategory.getLeftValue())
                .le(CustomerCategory::getRightValue, dbCategory.getRightValue())
        );
    }

}
