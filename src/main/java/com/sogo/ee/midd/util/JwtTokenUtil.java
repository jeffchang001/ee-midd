package com.sogo.ee.midd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String fixedToken;

    public boolean validateToken(String token) {
        return fixedToken.equals(token);
    }

    public String getFixedToken() {
        return fixedToken;
    }
}
