package org.finance.infrastructure.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "modifyBy", SecurityUtil::getUserId, Long.class);
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "modifyName", SecurityUtil::getUserName, String.class);
    }

}
