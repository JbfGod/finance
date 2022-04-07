package org.finance.infrastructure.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class RPage<T> implements Serializable {

    private long current;
    private List<T> data;
    private long pageSize;
    private boolean success;
    private long total;

    public RPage() {
        this.success = true;
    }

    public static <T> RPage<T> build(IPage<T> page) {
        return new RPage<T>().setCurrent(page.getCurrent())
                .setPageSize(page.getSize())
                .setData(page.getRecords())
                .setTotal(page.getTotal());
    }
}
