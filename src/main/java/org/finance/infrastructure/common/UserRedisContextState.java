package org.finance.infrastructure.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class UserRedisContextState implements Serializable {

    private Long proxyCustomerId;

}
