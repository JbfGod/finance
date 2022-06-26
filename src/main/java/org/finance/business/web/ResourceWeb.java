package org.finance.business.web;

import org.finance.business.convert.ResourceConvert;
import org.finance.business.service.ResourceService;
import org.finance.business.web.vo.TreeResourceVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceWeb {

    @Resource
    private ResourceService resourceService;

    @GetMapping("/treeOfNormalCustomer")
    public R<List<TreeResourceVO>> treeNormalCustomerResources() {
        List<Long> canGrantResourceIds = SecurityUtil.getCurrentUser().getResources().stream()
                .map(org.finance.business.entity.Resource::getId)
                .collect(Collectors.toList());
        return R.ok(ResourceConvert.INSTANCE.toTreeResourceVO(
                resourceService.list().stream()
                        .filter(r -> {
                            if (r.getSuperCustomer()) {
                                return false;
                            }
                            if (!canGrantResourceIds.contains(r.getId())) {
                                r.setDisabled(true);
                            }
                            return true;
                        })
                        .collect(Collectors.toList())
        ));
    }

}
