package com.shiro.demo.service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

public class JwtUtil {
    // 私有签名
    private static final String SING = "6Dx8SIuaHXJYnpsG18SSpjPs50lZcT52";

    public static String createJWT(String username, String issuer, String subject) {

        // 签名算法和秘钥
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SING);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        // 添加payload
        JwtBuilder builder = Jwts.builder().setId(username)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        // 指定令牌过期时间
        builder.setExpiration(instance.getTime());


        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * 解密
     *
     */
    public static Claims parseJWT(String jwt) {
        //This line will throw an exception if
        // it is not a signed JWS (as expected)

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SING))
                .parseClaimsJws(jwt)
                .getBody();
        return claims;

    }

    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}