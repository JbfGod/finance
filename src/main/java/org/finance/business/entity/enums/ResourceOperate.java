package org.finance.business.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public enum ResourceOperate {

    VIEW_ALL("view:all", "查询所有"),
    AUDITING("auditing", "审核"),
    UN_AUDITING("unAuditing", "弃审"),
    BOOKKEEPING("bookkeeping", "记账"),
    UN_BOOKKEEPING("unBookkeeping", "反记账"),
    BASE("base", "基本操作"),
    PRINT("print", "打印"),
    ADD_FOREIGN("addForeign", "添加外币凭证"),
    ;
    private String value;
    private String label;

    public final static String ID_PREFIX = "PERMIT_";
    ResourceOperate(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getId(long resourceId) {
        return String.format("%s,%s,%s", ID_PREFIX, resourceId, this.value);
    }

    public static void consumerResourceOperateId(String id, BiConsumer<Long, String> biConsumer) {
        String[] resourceOperateId = id.split(",");
        biConsumer.accept(Long.parseLong(resourceOperateId[1]), resourceOperateId[2]);
    }


    public Map<String, String> getResourceOperateMap() {
        return Arrays.stream(ResourceOperate.values()).collect(Collectors.toMap(ResourceOperate::getValue, ResourceOperate::getLabel));
    }

    public List<Map<String, String>> getResourceOperates() {
        List<Map<String, String>> list = new ArrayList<>();
        ResourceOperate[] values = ResourceOperate.values();
        for (ResourceOperate resourceOperate : values) {
            Map<String, String> map = new HashMap<>(1);
            map.put("label", resourceOperate.getLabel());
            map.put("value", resourceOperate.getValue());
            list.add(map);
        }
        return list;
    }

    public static String getLabelByCode(String code) {
        ResourceOperate[] values = ResourceOperate.values();
        for (ResourceOperate resourceOperate : values) {
            if (resourceOperate.getValue().equals(code)) {
                return resourceOperate.getLabel();
            }
        }
        return null;
    }

    public static ResourceOperate getByCode(String code) {
        ResourceOperate[] values = ResourceOperate.values();
        for (ResourceOperate resourceOperate : values) {
            if (resourceOperate.getValue().equals(code)) {
                return resourceOperate;
            }
        }
        return null;
    }

}
