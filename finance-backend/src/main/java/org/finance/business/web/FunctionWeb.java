package org.finance.business.web;

import org.finance.business.convert.FunctionConvert;
import org.finance.business.service.FunctionService;
import org.finance.business.web.vo.TreeFunctionVO;
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
@RequestMapping("/api/function")
public class FunctionWeb {

    @Resource
    private FunctionService functionService;

    @GetMapping("/tree")
    public R<List<TreeFunctionVO>> treeFunctions() {
        return R.ok(FunctionConvert.INSTANCE.toTreeFunctionsVO(functionService.list()));
    }

}
