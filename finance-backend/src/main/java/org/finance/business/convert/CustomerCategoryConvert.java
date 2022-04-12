package org.finance.business.convert;

import org.finance.business.entity.CustomerCategory;
import org.finance.business.web.request.AddCustomerCategoryRequest;
import org.finance.business.web.request.UpdateCustomerCategoryRequest;
import org.finance.business.web.vo.CustomerCategoryVO;
import org.finance.business.web.vo.TreeCustomerCategoryVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface CustomerCategoryConvert {

    CustomerCategoryConvert INSTANCE = Mappers.getMapper( CustomerCategoryConvert.class );

    CustomerCategory toCustomerCategory(AddCustomerCategoryRequest request);

    CustomerCategory toCustomerCategory(UpdateCustomerCategoryRequest request);

    TreeCustomerCategoryVO toTreeCustomerCategoryVO(CustomerCategory category);

    CustomerCategoryVO toCustomerCategoryVO(CustomerCategory category);

    default List<TreeCustomerCategoryVO> toTreeCustomerCategoryVO(List<CustomerCategory> categories) {
        List<TreeCustomerCategoryVO> treeCategories = categories.stream().map(this::toTreeCustomerCategoryVO)
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeCategories, TreeCustomerCategoryVO::getId, TreeCustomerCategoryVO::getParentId
                , TreeCustomerCategoryVO::getChildren, TreeCustomerCategoryVO::setChildren);
    }
}