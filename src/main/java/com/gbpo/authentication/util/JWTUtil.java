package com.gbpo.authentication.util;

import com.gbpo.authentication.filter.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

/**
 * Created by Shoaib on 19/02/2018.
 */
public class JWTUtil {
    public static final long ONE_DAY = 24*3600*1000;
    public static final long REFRESH_TOKEN_EXPIRY_TIME =  86000;

    public static String getTokenFromRequest(HttpServletRequest request) {
        Assert.notNull(request, CommonConstants.REQUEST_IS_NULL);
        return request.getHeader(SecurityConstants.HEADER_STRING);
    }

    public static String getUserNameFromRequest(String token) {
            return getTokenBody(token).getSubject();

    }

    public static HttpHeaders createHeaders(HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(SecurityConstants.HEADER_STRING, getTokenFromRequest(request));
        response.addHeader(SecurityConstants.HEADER_STRING, request.getHeader(SecurityConstants.HEADER_STRING));
        return headers;
    }

    public static boolean checkTokenTimeValidity(String token) {
        return getTokenExpiryTime(token).before(new Date(System.currentTimeMillis()));
    }

    public static Date getTokenExpiryTime(String token) {
        return getTokenBody(token).getExpiration();
    }

    public static Claims getTokenBody(String token) {
        try {
        return Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET.getBytes("UTF-8"))
            .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
            .getBody();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createRefreshToken(String token) {
        try {
            Claims claims = getTokenBody(token);
            claims.put("created", System.currentTimeMillis());
            return SecurityConstants.TOKEN_PREFIX + " " +
                Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(getRefreshExpiryDate())
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static Date getRefreshExpiryDate() {
        return new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY_TIME * 1000);
    }

    public static String createToken(String username) {
        String jwt = null;
        try {
            jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes("UTF-8"))
                .compact();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jwt;
    }

    public static void main(String[] args) {
        //JWTUtil.getTokenExpiry("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraGF3YWphLDI2ODYiLCJjcmVhdGVkIjoxNTE5MDI0Mzg3ODk2LCJleHAiOjE1MTkwMjQ0NzN9.RfwqsuxYyCpljaTncCOHMAdm9vTaZpRmfkXjvrUaQSpWtJ7qtkrQKr-kbedZ8yHvE276LG4cNDM8VnL2UQDUrg");
    }
}
