package org.finance.business.web.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbangfa
 */
@Data
public abstract class AbstractPageRequest implements Serializable {

    protected long current;
    protected long pageSize;

    public <T> Page<T> extractPage() {
        return Page.of(this.current, this.pageSize);
    }
}
