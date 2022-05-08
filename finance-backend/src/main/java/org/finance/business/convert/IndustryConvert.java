package org.finance.business.convert;

import org.finance.business.entity.Industry;
import org.finance.business.web.request.AddIndustryRequest;
import org.finance.business.web.request.UpdateIndustryRequest;
import org.finance.business.web.vo.IndustryVO;
import org.finance.business.web.vo.TreeIndustryVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface IndustryConvert {

    IndustryConvert INSTANCE = Mappers.getMapper( IndustryConvert.class );

    Industry toIndustry(AddIndustryRequest request);

    Industry toIndustry(UpdateIndustryRequest request);

    TreeIndustryVO toTreeIndustryVO(Industry industry);

    IndustryVO toIndustryVO(Industry industry);

    default List<TreeIndustryVO> toTreeIndustryVO(List<Industry> industries) {
        List<TreeIndustryVO> treeCategories = industries.stream().map(this::toTreeIndustryVO)
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeCategories, TreeIndustryVO::getId, TreeIndustryVO::getParentId
                , TreeIndustryVO::getChildren, TreeIndustryVO::setChildren);
    }
    
}
