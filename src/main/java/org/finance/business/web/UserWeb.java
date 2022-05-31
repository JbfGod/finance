package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.ResourceConvert;
import org.finance.business.convert.UserConvert;
import org.finance.business.entity.Resource;
import org.finance.business.entity.User;
import org.finance.business.service.CustomerResourceService;
import org.finance.business.service.UserResourceService;
import org.finance.business.service.UserService;
import org.finance.business.web.request.AddUserRequest;
import org.finance.business.web.request.GrantResourcesToUserRequest;
import org.finance.business.web.request.QueryUserRequest;
import org.finance.business.web.request.UpdateSelfPasswordRequest;
import org.finance.business.web.request.UpdateUserPasswordRequest;
import org.finance.business.web.request.UpdateUserRequest;
import org.finance.business.web.vo.UserListVO;
import org.finance.business.web.vo.UserOwnedMenuVO;
import org.finance.business.web.vo.UserSelfVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @javax.annotation.Resource
    private UserService userService;
    @javax.annotation.Resource
    private UserResourceService userResourceService;
    @javax.annotation.Resource
    private CustomerResourceService customerResourceService;

    @GetMapping("/self")
    public R<UserSelfVO> selfInfo() {
        return R.ok(UserConvert.INSTANCE.toUserSelfVO(userService.loadUserById(SecurityUtil.getCurrentUserId())));
    }

    @GetMapping("/self/menus")
    public R<List<UserOwnedMenuVO>> selfMenus() {
        return R.ok(ResourceConvert.INSTANCE.toTreeMenus(this.getSelfResources()));
    }

    @GetMapping("/self/permissions")
    public R<List<String>> selfPermission() {
        List<String> permissions = this.getSelfResources().stream()
                .map(ResourceConvert.INSTANCE::toAccess)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        return R.ok(permissions);
    }

    @GetMapping("/{userId}/resources")
    public R<List<Long>> resourceIdsOfUser(@PathVariable("userId") long userId) {
        List<Long> resourceIds = userResourceService.getResourcesByUserId(userId)
                .stream().filter(func -> !func.getHasLeaf())
                .map(Resource::getId)
                .collect(Collectors.toList());
        return R.ok(resourceIds);
    }

    @GetMapping("/page")
    public RPage<UserListVO> pageUser(QueryUserRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        IPage<UserListVO> pages = userService.page(request.extractPage(), Wrappers.<User>lambdaQuery()
                .eq(User::getRole, request.getRole())
                .eq(currentUser.getCustomerId() > 0, User::getCustomerId, currentUser.getCustomerId())
                .likeRight(StringUtils.hasText(request.getCustomerNumber()), User::getCustomerNumber, request.getCustomerNumber())
                .likeRight(StringUtils.hasText(request.getName()), User::getName, request.getName())
                .likeRight(StringUtils.hasText(request.getAccount()), User::getAccount, request.getAccount())
                .orderByDesc(User::getCreateBy)
        ).convert(UserConvert.INSTANCE::toUserListVO);
        return RPage.build(pages);
    }

    @PostMapping("/grantResources")
    public R grantResourcesToUser(@RequestBody @Valid GrantResourcesToUserRequest request) {
        userResourceService.grantResourcesToUser(request.getUserId(), request.getResourceIds());
        return R.ok();
    }

    @PostMapping("/add")
    public R addUser(@RequestBody @Valid AddUserRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        User user = UserConvert.INSTANCE.toUser(request, currentUser.getCustomerId(), currentUser.getCustomerNumber());
        userService.addUser(user);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateUser(@RequestBody @Valid UpdateUserRequest request) {
        User user = UserConvert.INSTANCE.toUser(request);
        userService.saveOrUpdate(user);
        return R.ok();
    }

    @PutMapping("/self/updatePassword")
    public R selfUpdatePassword(@RequestBody @Valid UpdateSelfPasswordRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        User dbUser = userService.getById(currentUser.getId());

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        AssertUtil.isTrue(dbUser.getPassword().equals(SecurityUtil.encodePassword(oldPassword)), "旧密码不正确！");

        userService.updatePassword(dbUser.getId(), newPassword);
        return R.ok();
    }

    @PutMapping("/resetPassword")
    public R resetUserPassword(@RequestBody @Valid UpdateUserPasswordRequest request) {
        userService.updatePassword(request.getId(), request.getPassword());
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteUser(@PathVariable("id") long id) {
        AssertUtil.isFalse(SecurityUtil.getCurrentUserId() == id, "不能删除当前操作用户！");
        userService.removeById(id);
        return R.ok();
    }

    private List<Resource> getSelfResources() {
        List<Resource> resources;
        User currentUser = SecurityUtil.getCurrentUser();
        // 若果是管理员用户直接获取所在客户单位的功能菜单
        if (currentUser.getRole() == User.Role.ADMIN && currentUser.getCustomerId() > 0) {
            resources = customerResourceService.listResourceByCustomerId(currentUser.getCustomerId());
        } else {
            resources = userResourceService.getResourcesByUserId(currentUser.getId());
        }
        return resources;
    }

}
