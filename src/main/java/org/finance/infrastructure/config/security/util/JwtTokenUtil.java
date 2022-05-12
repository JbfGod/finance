package org.finance.infrastructure.config.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author jiangbangfa
 */
public class JwtTokenUtil {

    private final static long EXPIRE_TIME = 50L * 365 * 24 * 3600 * 1000;
    private final static String SECRET = "e3fc07ef-1122-4b15-b368-7ef00b559b32";

    public static String generateTokenByUser(Long userId) {
        return JWT.create().withIssuedAt(new Date())
                .withSubject(userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String getUserIdAndVerifyByToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getSubject();
    }

    public static DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
    }

}
