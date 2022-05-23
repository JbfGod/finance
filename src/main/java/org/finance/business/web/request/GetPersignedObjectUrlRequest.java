package org.finance.business.web.request;

import lombok.Data;
import org.finance.infrastructure.constants.BucketName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jiangbangfa
 */
@Data
public class GetPersignedObjectUrlRequest {

    @NotNull(message = "请填写bucket ！")
    private BucketName bucket;
    @NotBlank(message = "请填写文件名！")
    private String fileName;
    private String objectName;

    public String getFileExtend() {
        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index);
        }
        return "";
    }
}
