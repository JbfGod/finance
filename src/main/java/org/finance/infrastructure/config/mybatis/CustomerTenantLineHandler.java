package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.util.SpringContextUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiangbangfa
 */
public class CustomerTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        User currentUser = null;
        try {
            currentUser = SecurityUtil.getCurrentUser();
            String headCustomerId = SpringContextUtil.getHttpServletRequest().getHeader(Constants.HEAD_CUSTOMER_ID);
            if (StringUtils.isNotBlank(headCustomerId) && currentUser.getCustomerId() == 0) {
                return new LongValue(headCustomerId);
            }
            return new LongValue(currentUser.getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getTenantIdColumn() {
        return "customer_id";
    }

    private final List<String> includeTables = Arrays.asList(
            "expense_bill", "expense_item", "expense_item_attachment", "expense_item_subsidy",
            "industry", "subject"
    );

    @Override
    public boolean ignoreTable(String tableName) {
        return !includeTables.contains(tableName.replace("`", ""));
    }

}