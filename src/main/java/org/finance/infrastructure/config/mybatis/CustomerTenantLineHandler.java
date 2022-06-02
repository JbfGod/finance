package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.SpringContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.finance.infrastructure.constants.Constants.DEFAULT_CUSTOMER_ID;

/**
 * @author jiangbangfa
 */
public class CustomerTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        User currentUser = SecurityUtil.getCurrentUserOfNullable();
        if (currentUser == null) {
            return null;
        }
        HttpServletRequest request = SpringContextUtil.getHttpServletRequest();
        if (request == null) {
            return null;
        }
        String headCustomerId = request.getHeader("CustomerId");
        if (StringUtils.isNotBlank(headCustomerId) && currentUser.getCustomerId() == DEFAULT_CUSTOMER_ID) {
            return new LongValue(headCustomerId);
        }
        return new LongValue(currentUser.getCustomerId());
    }

    @Override
    public String getTenantIdColumn() {
        return "customer_id";
    }

    private final List<String> includeTables = Arrays.asList(
            "expense_bill", "expense_item", "expense_item_attachment", "expense_item_subsidy",
            "industry", "subject", "user", "voucher", "voucher_item", "currency"
    );

    @Override
    public boolean ignoreTable(String tableName) {
        if (SpringContextUtil.getHttpServletRequest() == null) {
            return true;
        }
        if (SecurityUtil.getCurrentUserOfNullable() == null) {
            return true;
        }
        return !includeTables.contains(tableName.replace("`", ""));
    }

}