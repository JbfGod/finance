package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;

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

    private final List<String> ignoreTables = Arrays.asList(
        "user", "sequence", "function"
    );

    @Override
    public boolean ignoreTable(String tableName) {
        return ignoreTables.contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

}