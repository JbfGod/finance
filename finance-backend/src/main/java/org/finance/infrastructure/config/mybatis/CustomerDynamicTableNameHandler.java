package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.SecurityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author jiangbangfa
 */
public class CustomerDynamicTableNameHandler implements TableNameHandler {

    public final List<String> notSupportTables = Arrays.asList("user");

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (notSupportTables.contains(tableName)) {
            return tableName;
        }
        User currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null || currentUser.getCustomerId() == 0) {
            return tableName;
        }
        return String.format("%s_%s", tableName, currentUser.getCustomerId());
    }

}
