package org.finance.business.convert;

import org.finance.business.entity.Resource;
import org.finance.business.web.vo.ResourceIdentifiedVO;
import org.finance.business.web.vo.TreeResourceVO;
import org.finance.business.web.vo.TreeResourceWithOperateVO;
import org.finance.business.web.vo.UserOwnedMenuVO;
import org.finance.infrastructure.util.CollectionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    TreeResourceWithOperateVO toTreeResourceWithOperateVO(Resource resource);
    default List<TreeResourceWithOperateVO> toTreeResourceWithOperateVO(List<Resource> resources) {
        List<TreeResourceWithOperateVO> operateList = resources.stream().map(this::toTreeResourceWithOperateVO)
                .collect(Collectors.toList());
        /*List<TreeResourceWithOperateVO> operateList = resources.stream().flatMap(r -> {
            TreeResourceWithOperateVO operateVO = this.toTreeResourceWithOperateVO(r);
            String permitCode = r.getPermitCode();
            if (!StringUtils.hasText(permitCode)) {
                return Stream.of(operateVO);
            }
            List<TreeResourceWithOperateVO> list = new ArrayList<>();
            list.add(operateVO);
            String[] operateCode = permitCode.split(",");
            for (String code : operateCode) {
                ResourceOperate resourceOperate = ResourceOperate.getByCode(code);
                list.add(new TreeResourceWithOperateVO()
                        .setId(resourceOperate.getId(r.getId()))
                        .setName(resourceOperate.getLabel())
                        .setParentId(r.getId().toString())
                        .setSortNum(0)
                );
            }
            return list.stream();
        }).collect(Collectors.toList());*/
        return CollectionUtil.transformTree(operateList, TreeResourceWithOperateVO::getId, TreeResourceWithOperateVO::getParentId
                , TreeResourceWithOperateVO::getChildren, TreeResourceWithOperateVO::setChildren);
    }

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

    default Stream<String> toAccess(Resource r) {
        String permitCode = r.getPermitCode();
        if (StringUtils.hasText(permitCode)) {
            return Arrays.stream(r.getPermitCode().split(","))
                    .map(code -> String.join(":", r.getModule().name(), code));
        }
        return Stream.of("");
    }
}
