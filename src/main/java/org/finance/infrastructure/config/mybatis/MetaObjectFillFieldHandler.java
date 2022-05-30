package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.finance.business.entity.User;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author jiangbangfa
 */
@Component
public class MetaObjectFillFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", SecurityUtil::getUserId, Long.class);
        this.strictInsertFill(metaObject, "creatorName", SecurityUtil::getUserName, String.class);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "modifyBy", SecurityUtil::getUserId, Long.class);
        this.strictInsertFill(metaObject, "modifyName", SecurityUtil::getUserName, String.class);
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "customerId", SecurityUtil::getCustomerId,Long.class);
        this.strictInsertFill(metaObject, "customerNumber", SecurityUtil::getCustomerNumber,String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "modifyBy", SecurityUtil::getUserId, Long.class);
        this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "modifyName", SecurityUtil::getUserName, String.class);

        this.strictUpdateFill(metaObject, "bookkeepingBy", () -> {
            User user = SecurityUtil.getCurrentUser();
            if (Objects.equals(metaObject.getValue("bookkeeping"), true)) {
                this.strictUpdateFill(metaObject, "bookkeeper", user::getName, String.class);
                return user.getId();
            } else if (Objects.equals(metaObject.getValue("bookkeeping"), false)) {
                this.strictUpdateFill(metaObject, "bookkeeper", () -> "", String.class);
                return 0L;
            }
            return null;
        }, Long.class);

        this.strictUpdateFill(metaObject, "auditBy", () -> {
            User user = SecurityUtil.getCurrentUser();
            if (Objects.equals(metaObject.getValue("auditStatus"), true)) {
                this.strictUpdateFill(metaObject, "auditor", user::getName, String.class);
                return user.getId();
            } else if (Objects.equals(metaObject.getValue("auditStatus"), false)) {
                this.strictUpdateFill(metaObject, "auditor", () -> "", String.class);
                return 0L;
            }
            return null;
        }, Long.class);

    }

}
