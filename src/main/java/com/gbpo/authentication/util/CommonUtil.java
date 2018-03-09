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

    public static <D> D mapSourceClassToDestination(Object sourceClassObj, Class<D> destinationClass) {
        Assert.notNull(sourceClassObj, CommonConstants.SOURCE_CLASS_NULL_OBJECT);
        return new ModelMapper().map(sourceClassObj, destinationClass);
    }
}
