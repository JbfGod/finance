package org.finance.business.convert;

import org.finance.business.entity.UserResource;
import org.finance.business.web.request.GrantResourcesToUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface UserResourceConvert {

    UserResourceConvert INSTANCE = Mappers.getMapper( UserResourceConvert.class );

    default List<UserResource> toUserResource(GrantResourcesToUserRequest request) {
        return request.getResourceIds().stream()
                .map(funcId -> new UserResource().setUserId(request.getUserId()).setResourceId(funcId))
                .collect(Collectors.toList());
    }

}
