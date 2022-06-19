package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
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
        this.strictInsertFill(metaObject, "customerId", SecurityUtil::getOperateCustomerId, Long.class);
        this.strictInsertFill(metaObject, "customerNumber", SecurityUtil::getOperateCustomerNumber, String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "modifyBy", SecurityUtil::getUserId, Long.class);
        this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "modifyName", SecurityUtil::getUserName, String.class);

        this.strictUpdateFill(metaObject, "bookkeepingBy", () -> {
            if (Objects.equals(metaObject.getValue("bookkeeping"), true)) {
                return SecurityUtil.getUserId();
            }
            return 0L;
        }, Long.class);

        this.strictUpdateFill(metaObject, "bookkeeperName", () -> {
            if (Objects.equals(metaObject.getValue("bookkeeping"), true)) {
                return SecurityUtil.getUserName();
            }
            return "";
        }, String.class);

        this.strictUpdateFill(metaObject, "auditorName", () -> {
            if (Objects.equals(metaObject.getValue("auditStatus"), true)) {
                return SecurityUtil.getUserName();
            }
            return "";
        }, String.class);

        this.strictUpdateFill(metaObject, "auditBy", () -> {
            if (Objects.equals(metaObject.getValue("auditStatus"), true)) {
                return SecurityUtil.getUserId();
            }
            return 0L;
        }, Long.class);

    }

}
