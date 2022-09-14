package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.finance.business.entity.Customer;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.SpringContextUtil;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiangbangfa
 */
public class CustomerTenantLineHandler implements TenantLineHandler {

    private final AntPathMatcher ignoreAntMatcher = new AntPathMatcher();
    @Override
    public Expression getTenantId() {
        User currentUser = SecurityUtil.getCurrentUserOfNullable();
        if (currentUser == null) {
            return null;
        }
        if (Customer.DEFAULT_ID.equals(currentUser.getCustomerId())) {
            return new LongValue(SecurityUtil.getProxyCustomerId());
        }
        return new LongValue(currentUser.getCustomerId());
    }

    @Override
    public String getTenantIdColumn() {
        return "customer_id";
    }

    private final List<String> includeTables = Arrays.asList(
            "expense_bill", "expense_item", "expense_item_attachment", "expense_item_subsidy",
            "subject", "voucher", "voucher_item", "currency"
    );

    private final List<String> ignoreUrls = Arrays.asList(
            "/api/user/self", "/api/expense/bill/page/approval", "/api/expense/bill/{id:\\d+}",
            "/api/approvalFlow/**", "/api/approvalInstance/**"
    );

    @Override
    public boolean ignoreTable(String tableName) {
        HttpServletRequest request = SpringContextUtil.getHttpServletRequest();
        if (request == null) {
            return true;
        }
        if (SecurityUtil.getCurrentUserOfNullable() == null) {
            return true;
        }
        for (String ignoreUrl : ignoreUrls) {
            if (ignoreAntMatcher.match(ignoreUrl, request.getRequestURI())) {
                return true;
            }
        }
        return !includeTables.contains(tableName.replace("`", ""));
    }

}