package org.finance.infrastructure.config.security;

import com.alibaba.fastjson.JSON;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiangbangfa
 */
@Component
public class UserLogoutHandler implements LogoutHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationToken = request.getHeader("Authorization").substring("Bearer ".length());
        String cacheKey = CacheKeyUtil.getToken(authorizationToken).getKey();
        redisTemplate.delete(cacheKey);

        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(R.ok()));
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
