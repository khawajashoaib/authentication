package com.gbpo.authentication.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Asad on 2/19/2018.
 */

@Configuration
@Data
public class ApplicationConfig {
    @Value("${service.database}")
    private String serviceDatabase;

}
