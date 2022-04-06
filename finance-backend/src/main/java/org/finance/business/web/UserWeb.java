package org.finance.business.web;

import org.finance.business.convert.FunctionConvert;
import org.finance.business.convert.UserConvert;
import org.finance.business.entity.Function;
import org.finance.business.entity.User;
import org.finance.business.service.UserService;
import org.finance.business.web.vo.UserOwnedMenuVO;
import org.finance.business.web.vo.UserVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/user")
public class UserWeb {

    @Resource
    private UserService userService;

    @GetMapping("/self")
    public R<UserVO> selfInfo() {
        return R.ok(UserConvert.INSTANCE.toUserVO(userService.loadUserById(SecurityUtil.getCurrentUserId())));
    }

    @GetMapping("/self/menus")
    public R<List<UserOwnedMenuVO>> selfMenus() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<UserOwnedMenuVO> ownedMenus = FunctionConvert.INSTANCE.toTreeMenus(
                userService.getFunctionsByUserId(currentUser.getId()));
        return R.ok(ownedMenus);
    }

    @GetMapping("/self/permissions")
    public R<List<String>> selfPermission() {
        User currentUser = SecurityUtil.getCurrentUser();
        List<String> permissions = userService.getFunctionsByUserId(currentUser.getId())
                .stream().map(Function::getPermitCode)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        return R.ok(permissions);
    }

}
