package org.finance.business.web;

import org.finance.business.entity.User;
import org.finance.business.service.UserService;
import org.finance.business.web.vo.TreeFunctionVO;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/self/functions")
    public ResponseEntity<List<TreeFunctionVO>> selfTreeFunctions() {
        User currentUser = SecurityUtil.getCurrentUser();
        return ResponseEntity.ok(userService.getTreeFunctionsByUserId(currentUser.getId()));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TreeFunctionVO>> list() {
        User currentUser = SecurityUtil.getCurrentUser();
        return ResponseEntity.ok(userService.getTreeFunctionsByUserId(currentUser.getId()));
    }

}
