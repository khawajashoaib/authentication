package com.gbpo.authentication.util;

import com.gbpo.authentication.filter.SecurityConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

/**
 * Created by Shoaib on 16/02/2018.
 */
public class CommonUtil  {
    public static final long ONE_DAY = 24*3600*1000;
    public static HttpHeaders createHeaders(HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.add(SecurityConstants.HEADER_STRING, request.getHeader(SecurityConstants.HEADER_STRING));
        response.addHeader(SecurityConstants.HEADER_STRING, request.getHeader(SecurityConstants.HEADER_STRING));
        return headers;
    }

    public static <D> D mapSourceClassToDestination(Object sourceClassObj, Class<D> destinationClass) {
        Assert.notNull(sourceClassObj, "Source Class Object Cannot be null");
        return new ModelMapper().map(sourceClassObj, destinationClass);
    }
}
