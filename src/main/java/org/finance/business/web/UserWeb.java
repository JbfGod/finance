package org.finance.business.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CustomerConvert;
import org.finance.business.convert.ResourceConvert;
import org.finance.business.convert.UserConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Resource;
import org.finance.business.entity.User;
import org.finance.business.entity.enums.ResourceOperate;
import org.finance.business.service.CustomerResourceService;
import org.finance.business.service.CustomerService;
import org.finance.business.service.UserResourceService;
import org.finance.business.service.UserService;
import org.finance.business.web.request.AddUserRequest;
import org.finance.business.web.request.GrantResourcesToUserRequest;
import org.finance.business.web.request.QueryOwnedCustomerRequest;
import org.finance.business.web.request.QueryUserRequest;
import org.finance.business.web.request.UpdateSelfPasswordRequest;
import org.finance.business.web.request.UpdateUserPasswordRequest;
import org.finance.business.web.request.UpdateUserRequest;
import org.finance.business.web.vo.CustomerCueVO;
import org.finance.business.web.vo.UserListVO;
import org.finance.business.web.vo.UserOwnedMenuVO;
import org.finance.business.web.vo.UserSelfVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.handler.MyPermissionEvaluator;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @javax.annotation.Resource
    private CustomerService customerService;

    @javax.annotation.Resource
    private MyPermissionEvaluator myPermissionEvaluator;

    @GetMapping("/self")
    public R<UserSelfVO> selfInfo() {
        User user = SecurityUtil.getCurrentUser();
        UserSelfVO userSelfVO = UserConvert.INSTANCE.toUserSelfVO(user);
        return R.ok(userSelfVO);
    }

    @GetMapping("/self/menus")
    public R<List<UserOwnedMenuVO>> selfMenus() {
        return R.ok(ResourceConvert.INSTANCE.toTreeMenus(this.getSelfResources()));
    }

    @GetMapping("/self/permissions")
    public R<List<String>> selfPermission() {
        return R.ok(SecurityUtil.getPermissions());
    }

    @GetMapping("/{userId}/resources")
    public R<List<String>> resourceIdsOfUser(@PathVariable("userId") long userId) {
        List<String> resourceIds = userResourceService.getResourcesByUserId(userId)
                .stream()
                .flatMap(r -> {
                    Long id = r.getId();
                    String permitCode = r.getPermitCode();
                    if (!StringUtils.hasText(permitCode)) {
                        return Stream.of(id.toString());
                    }
                    return Arrays.stream(permitCode.split(","))
                            .map(code -> ResourceOperate.getByCode(code).getId(r.getId()));
                })
                .collect(Collectors.toList());
        return R.ok(resourceIds);
    }

    @GetMapping("/page")
    public RPage<UserListVO> pageUser(QueryUserRequest request) {
        Function<Long, String> getCustomerNameById = customerService.getCustomerNameFunction();
        IPage<UserListVO> pages = userService.page(request.extractPage(), this.buildQueryWrapperByRequest(request))
                .convert((user -> {
                    String customerName = getCustomerNameById.apply(user.getCustomerId());
                    UserListVO userListVO = UserConvert.INSTANCE.toUserListVO(user);
                    userListVO.setCustomerName(customerName);
                    return userListVO;
                }));
        return RPage.build(pages);
    }

    @GetMapping("/listFromSuperCustomer")
    public R<List<UserListVO>> listUserFromSuperCustomer() {
        return R.ok(userService.list(Wrappers.<User>lambdaQuery()
                        .eq(User::getCustomerId, Customer.DEFAULT_ID)
                )
                .stream().map(UserConvert.INSTANCE::toUserListVO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/ownedCustomer")
    public R<List<CustomerCueVO>> ownedCustomer(QueryOwnedCustomerRequest request) {
        Long userId = SecurityUtil.getUserId();
        boolean searchAll = myPermissionEvaluator.hasPermission(SecurityUtil.getAuthentication(),
                "customer", ResourceOperate.VIEW_ALL.getValue()
        );
        List<Customer> customers = customerService.list(Wrappers.<Customer>lambdaQuery()
            .eq(!searchAll, Customer::getBusinessUserId, userId)
            .gt(Customer::getId, 0)
            .likeRight(StringUtils.hasText(request.getCustomerName()), Customer::getName, request.getCustomerName())
            .likeRight(StringUtils.hasText(request.getCustomerNumber()), Customer::getNumber, request.getCustomerNumber())
        );
        return R.ok(customers.stream()
            .map(CustomerConvert.INSTANCE::toCustomerCueVO)
            .collect(Collectors.toList())
        );
    }

    @PostMapping("/grantResources")
    public R grantResourcesToUser(@RequestBody @Valid GrantResourcesToUserRequest request) {
        userResourceService.grantResourcesToUser(request.getUserId(), request.getResourceWithOperateIds());
        return R.ok();
    }

    @PostMapping("/add")
    public R addUser(@RequestBody @Valid AddUserRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        User user = UserConvert.INSTANCE.toUser(request, currentUser.getCustomerId(), currentUser.getCustomerNumber());
        String customerNumber = user.getCustomerNumber();
        if (StringUtils.hasText(customerNumber)) {
            Customer dbCustomer = customerService.getOne(Wrappers.<Customer>lambdaQuery()
                    .select(Customer::getId)
                    .eq(Customer::getNumber, customerNumber)
            );
            AssertUtil.notNull(dbCustomer, "客户单位不存在！");
            user.setCustomerId(dbCustomer.getId());
        }
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

    public R deleteUser(@PathVariable("id") long id) {
        AssertUtil.isFalse(SecurityUtil.getCurrentUserId() == id, "不能删除当前操作用户！");
        userService.removeById(id);
        return R.ok();
    }

    @PutMapping("/switch/proxyCustomer")
    public R switchProxyCustomer(Long customerId, @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring("Bearer ".length());
        userService.proxyCustomer(SecurityUtil.getUserId(), customerId, token);
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

    private LambdaQueryWrapper<User> buildQueryWrapperByRequest(QueryUserRequest request) {
        User currentUser = SecurityUtil.getCurrentUser();
        boolean isSuperCustomer = SecurityUtil.isSuperCustomer();
        LambdaQueryWrapper<User> condition = Wrappers.<User>lambdaQuery()
                .eq(!isSuperCustomer, User::getCustomerId, currentUser.getCustomerId())
                .eq(request.getRole() != null, User::getRole, request.getRole())
                .likeRight(StringUtils.hasText(request.getCustomerNumber()), User::getCustomerNumber, request.getCustomerNumber())
                .likeRight(StringUtils.hasText(request.getName()), User::getName, request.getName())
                .likeRight(StringUtils.hasText(request.getAccount()), User::getAccount, request.getAccount())
                .orderByDesc(User::getCreateBy);
        if (StringUtils.hasText(request.getCustomerName())) {
            List<Long> ids = customerService.getIdsLikeByName(request.getCustomerName());
            condition.in(ids.size() > 0, User::getCustomerId, ids);
        }
        return condition;
    }

}
