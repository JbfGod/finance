package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiangbangfa
 */
public class CustomerDynamicTableNameHandler implements TableNameHandler {

    public final List<String> supportTables = Arrays.asList("subject", "industry");

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (!supportTables.contains(tableName)) {
            return tableName;
        }
        User user = null;
        try {
            user = SecurityUtil.getCurrentUser();
        } catch (Exception e) {
            return tableName;
        }
        if (user.getCustomerId() == 0) {
            return tableName;
        }
        return String.format("%s_%s", tableName, user.getCustomer().getTableIdentified());
    }

}
