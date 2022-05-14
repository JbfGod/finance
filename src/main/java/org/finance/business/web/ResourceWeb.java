package org.finance.business.web;

import org.finance.business.convert.ResourceConvert;
import org.finance.business.service.ResourceService;
import org.finance.business.web.vo.TreeResourceVO;
import org.finance.infrastructure.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/tree")
    public R<List<TreeResourceVO>> treeResources() {
        return R.ok(ResourceConvert.INSTANCE.toTreeResourceVO(resourceService.list()));
    }

}
