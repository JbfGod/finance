package org.finance.business.convert;

import org.finance.business.entity.Resource;
import org.finance.business.web.vo.ResourceIdentifiedVO;
import org.finance.business.web.vo.TreeResourceVO;
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
public interface ResourceConvert {

    ResourceConvert INSTANCE = Mappers.getMapper( ResourceConvert.class );

    @Mapping(source = "number", target = "key")
    @Mapping(source = "url", target = "path")
    @Mapping(source = "parentNumber", target = "parentKey")
    UserOwnedMenuVO toUserOwnedMenuVO(Resource f);

    TreeResourceVO toTreeResourceVO(Resource resource);

    ResourceIdentifiedVO toResourceIdentifiedVO(Resource resource);

    default List<TreeResourceVO> toTreeResourceVO(List<Resource> resources) {
        List<TreeResourceVO> treeResources = resources.stream().map(this::toTreeResourceVO).collect(Collectors.toList());
        return CollectionUtil.transformTree(treeResources, TreeResourceVO::getNumber, TreeResourceVO::getParentNumber
                , TreeResourceVO::getChildren, TreeResourceVO::setChildren);
    }

    default List<UserOwnedMenuVO> toTreeMenus(List<Resource> resources) {
        List<UserOwnedMenuVO> treeResources = resources.stream()
                .filter(func -> func.getType() == Resource.Type.MENU)
                .map(this::toUserOwnedMenuVO)
                .collect(Collectors.toList());
        return CollectionUtil.transformTree(treeResources, UserOwnedMenuVO::getKey, UserOwnedMenuVO::getParentKey
                , UserOwnedMenuVO::getChildren, UserOwnedMenuVO::setChildren);
    }

    default String toAccess(Resource res) {
        Resource.Type type = res.getType();
        if (type == Resource.Type.MENU) {
            return String.format("%s:%s", Resource.Type.MENU.name(), res.getUrl());
        } else if (type == Resource.Type.PERMIT) {
            return String.format("%s:%s", Resource.Type.PERMIT.name(), res.getUrl());
        }
        return "";
    }
}
