package org.finance.business.web;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.finance.business.web.request.GetPersignedObjectUrlRequest;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.util.SnowflakeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangbangfa
 */
@RestController
@RequestMapping("/api/minio")
public class MinioWeb {

    private final Pattern urlPattern = Pattern.compile("^https?://[\\.:\\d\\w]+/(.+)");
    @Resource
    private MinioClient minioClient;

    @GetMapping("/getPersignedObjectUrl")
    public R<String> persignedObjectUrl(@Valid GetPersignedObjectUrlRequest request) throws Exception {
        String identifiedId = SnowflakeUtil.nextIdStr();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-type", "application/json");
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                new GetPresignedObjectUrlArgs.Builder()
                        .method(Method.POST)
                        .bucket(request.getBucket().getName())
                        .expiry(5, TimeUnit.MINUTES)
                        .object(String.format("%s%s", identifiedId, request.getFileExtend()))
                        //.extraQueryParams(reqParams)
                        .build()
        );
        Matcher matcher = urlPattern.matcher(presignedObjectUrl);
        if (matcher.matches()) {
            return R.ok(matcher.group(1));
        }
        return R.ok(presignedObjectUrl);
    }

}
