package com.rl.renoj.utils;

import com.rl.renoj.annotation.AuthCheck;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 提供JWT生成的Token
 * @date 2024/2/5 19:24:14
 */
@Component
public class JwtTokenProvider {

    private final String secret="renlei";

    private long expirationTime=3000;



    // 生成Token

    public String generateToken(long userId)
    {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(SignatureAlgorithm.HS512,secret )
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .compact();
    }
}
