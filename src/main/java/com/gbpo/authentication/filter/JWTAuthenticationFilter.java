package com.gbpo.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbpo.authentication.model.UserModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoaib on 16/02/2018.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;
    private String email;

    public JWTAuthenticationFilter(AuthenticationManager manager) {
        this.authenticationManager = manager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserModel.class);
            creds.setEmail(creds.getEmail()+","+creds.getCompanyId());
            this.setEmail(creds.getEmail());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> prepareClaims() {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", this.getEmail());
        claims.put("created", new Date(System.currentTimeMillis()));
        return  claims;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        System.out.println(" User Name issSsss   " +
                this.getEmail());
        String token = Jwts.builder()
                .setSubject(this.getEmail())
                .setClaims(prepareClaims())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes("UTF-8"))
                .compact();
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + " " + token);
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


