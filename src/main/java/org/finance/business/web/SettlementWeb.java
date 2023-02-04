package org.finance.business.web;

import org.finance.business.entity.Customer;
import org.finance.business.service.CustomerService;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 结账
 * @author jiangbangfa
 */
@RestController
@RequestMapping("/api/settlement")
public class SettlementWeb {

    @Resource
    private CustomerService customerService;

    @PutMapping("/closingToNextPeriod")
    public R closingToNextPeriod() {
        Customer proxyCustomer = SecurityUtil.getProxyCustomer();
        Customer customer = customerService.getById(proxyCustomer.getId());
        customer.closingToNextPeriod();
        customerService.updateById(customer);
        return R.ok();
    }

    @PutMapping("/unClosingToPrevPeriod")
    public R unClosingToPrevPeriod() {
        Customer proxyCustomer = SecurityUtil.getProxyCustomer();
        Customer customer = customerService.getById(proxyCustomer.getId());
        customer.closingToPrevPeriod();
        customerService.updateById(customer);
        return R.ok();
    }


}
