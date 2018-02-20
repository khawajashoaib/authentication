package com.gbpo.authentication.util;

import com.gbpo.authentication.filter.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by Shoaib on 19/02/2018.
 */
public class JWTUtil {

    public static String getTokenExpiry(String token) {
        try {
            Date expirationDate = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET.getBytes("UTF-8"))
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getBody().getExpiration();
            System.out.println("expiration of this token is " + expirationDate );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ExpiredJwtException expiredToken) {
            System.out.println(" Expired Token Exception " + expiredToken.getClaims().getExpiration());
        }
        return null;
    }

    public static void main(String[] args) {
        JWTUtil.getTokenExpiry("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraGF3YWphLDI2ODYiLCJjcmVhdGVkIjoxNTE5MDI0Mzg3ODk2LCJleHAiOjE1MTkwMjQ0NzN9.RfwqsuxYyCpljaTncCOHMAdm9vTaZpRmfkXjvrUaQSpWtJ7qtkrQKr-kbedZ8yHvE276LG4cNDM8VnL2UQDUrg");
    }
}
