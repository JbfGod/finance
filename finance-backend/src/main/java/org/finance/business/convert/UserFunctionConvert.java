package org.finance.business.convert;

import org.finance.business.entity.UserFunction;
import org.finance.business.web.request.GrantFunctionsToUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Mapper
public interface UserFunctionConvert {

    UserFunctionConvert INSTANCE = Mappers.getMapper( UserFunctionConvert.class );

    default List<UserFunction> toUserFunction(GrantFunctionsToUserRequest request) {
        return request.getFunctionIds().stream()
                .map(funcId -> new UserFunction().setUserId(request.getUserId()).setFunctionId(funcId))
                .collect(Collectors.toList());
    }

}
