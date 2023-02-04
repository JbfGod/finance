package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.entity.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 科目表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    default List<Subject> listOrderByTree() {
        return this.selectList(Wrappers.<Subject>lambdaQuery().orderByAsc(Subject::getRootNumber, Subject::getNumber));
    }
}
