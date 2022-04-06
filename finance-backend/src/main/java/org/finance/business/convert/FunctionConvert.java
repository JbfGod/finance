package org.finance.business.convert;

import org.finance.business.entity.Function;
import org.finance.business.web.vo.UserOwnedMenuVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author jiangbangfa
 */
@Mapper
public interface FunctionConvert {

    FunctionConvert INSTANCE = Mappers.getMapper( FunctionConvert.class );


    @Mapping(source = "number", target = "key")
    @Mapping(source = "url", target = "path")
    @Mapping(source = "parentNumber", target = "parentKey")
    UserOwnedMenuVO toUserOwnedMenuVO(Function f);

    default List<UserOwnedMenuVO> toTreeMenus(List<Function> functions) {
        List<UserOwnedMenuVO> treeFunctions = functions.stream()
                .filter(func -> func.getType() == Function.Type.MENU)
                .map(this::toUserOwnedMenuVO)
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeFunctions, UserOwnedMenuVO::getKey, UserOwnedMenuVO::getParentKey
                , UserOwnedMenuVO::getChildren, UserOwnedMenuVO::setChildren);
    }
}
