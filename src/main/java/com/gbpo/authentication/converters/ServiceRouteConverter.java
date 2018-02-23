package com.gbpo.authentication.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbpo.authentication.bo.ServiceRoute;
import java.io.IOException;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Asad on 2/16/2018.
 */
@Component
@ConfigurationPropertiesBinding
public class ServiceRouteConverter implements Converter<String, List<ServiceRoute>> {

    @Override
    public List<ServiceRoute> convert(String s) {
        List<ServiceRoute> serviceRouteList = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            serviceRouteList = mapper.readValue(s, new TypeReference<List<ServiceRoute>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serviceRouteList;
    }
}
