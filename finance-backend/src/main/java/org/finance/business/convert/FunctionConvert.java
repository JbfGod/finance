package org.finance.business.convert;

import org.finance.business.entity.Function;
import org.finance.business.web.vo.TreeFunctionVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface FunctionConvert {

    FunctionConvert INSTANCE = Mappers.getMapper( FunctionConvert.class );

    TreeFunctionVO toTreeFunctionVO(Function function);

    default List<TreeFunctionVO> toTreeFunctionsVO(List<Function> functions) {
        List<TreeFunctionVO> treeFunctions = functions.stream().map(this::toTreeFunctionVO).collect(Collectors.toList());
        return CollectionUtil.transformTree(treeFunctions, TreeFunctionVO::getNumber, TreeFunctionVO::getParentNumber
                , TreeFunctionVO::getChildren, TreeFunctionVO::setChildren);
    }


}
