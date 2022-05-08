package org.finance.business.convert;

import javax.annotation.Generated;
import org.finance.business.entity.CustomerCategory;
import org.finance.business.web.request.AddCustomerCategoryRequest;
import org.finance.business.web.request.UpdateCustomerCategoryRequest;
import org.finance.business.web.vo.CustomerCategoryVO;
import org.finance.business.web.vo.TreeCustomerCategoryVO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-08T14:23:21+0800",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.1.1.jar, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class CustomerCategoryConvertImpl implements CustomerCategoryConvert {

    @Override
    public CustomerCategory toCustomerCategory(AddCustomerCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        CustomerCategory customerCategory = new CustomerCategory();

        customerCategory.setNumber( request.getNumber() );
        customerCategory.setName( request.getName() );
        customerCategory.setParentId( request.getParentId() );
        customerCategory.setRemark( request.getRemark() );

        return customerCategory;
    }

    @Override
    public CustomerCategory toCustomerCategory(UpdateCustomerCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        CustomerCategory customerCategory = new CustomerCategory();

        customerCategory.setId( request.getId() );
        customerCategory.setName( request.getName() );
        customerCategory.setRemark( request.getRemark() );

        return customerCategory;
    }

    @Override
    public TreeCustomerCategoryVO toTreeCustomerCategoryVO(CustomerCategory category) {
        if ( category == null ) {
            return null;
        }

        TreeCustomerCategoryVO treeCustomerCategoryVO = new TreeCustomerCategoryVO();

        treeCustomerCategoryVO.setId( category.getId() );
        treeCustomerCategoryVO.setNumber( category.getNumber() );
        treeCustomerCategoryVO.setName( category.getName() );
        treeCustomerCategoryVO.setHasLeaf( category.getHasLeaf() );
        treeCustomerCategoryVO.setParentNumber( category.getParentNumber() );
        treeCustomerCategoryVO.setParentId( category.getParentId() );
        treeCustomerCategoryVO.setRemark( category.getRemark() );

        return treeCustomerCategoryVO;
    }

    @Override
    public CustomerCategoryVO toCustomerCategoryVO(CustomerCategory category) {
        if ( category == null ) {
            return null;
        }

        CustomerCategoryVO customerCategoryVO = new CustomerCategoryVO();

        customerCategoryVO.setId( category.getId() );
        customerCategoryVO.setNumber( category.getNumber() );
        customerCategoryVO.setName( category.getName() );
        customerCategoryVO.setHasLeaf( category.getHasLeaf() );
        customerCategoryVO.setParentNumber( category.getParentNumber() );
        customerCategoryVO.setParentId( category.getParentId() );
        customerCategoryVO.setRemark( category.getRemark() );

        return customerCategoryVO;
    }
}
