package org.finance.business.web.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiangbangfa
 */
@Data
public class UserOwnedMenuVO {

    private String key;
    private String path;
    private String name;
    private String icon;
    private String parentKey;
    private List<UserOwnedMenuVO> children;
    private boolean locale;

}
