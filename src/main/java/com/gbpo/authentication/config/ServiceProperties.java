package com.gbpo.authentication.config;

import com.gbpo.authentication.bo.ServiceRoute;
import com.gbpo.authentication.multidb.CompanyAwareRoutingSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * Created by Asad on 2/15/2018.
 */
@Component
@ConfigurationProperties("routes")
@EnableConfigurationProperties
@Data
@RefreshScope
public class ServiceProperties {
    private List<ServiceRoute> list;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    @RefreshScope
    public DataSource dataSource() {
        AbstractRoutingDataSource dataSource = new CompanyAwareRoutingSource();
        Map<Object,Object> targetDataSources = new HashMap<>();
        list.forEach(serviceRoute -> {
            BasicDataSource basicDataSource = getBasicDataSource(serviceRoute);
            targetDataSources.put(serviceRoute.getCompanyId(), basicDataSource);
        });

        dataSource.setTargetDataSources(targetDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    private BasicDataSource getBasicDataSource(ServiceRoute serviceRoute){
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername(serviceRoute.getConnectionPool().getUsername());
        basicDataSource.setPassword(serviceRoute.getConnectionPool().getPassword());
        basicDataSource.setUrl("jdbc:mysql://" + serviceRoute.getIp()+":"+serviceRoute.getPort() + "/" +
            applicationConfig.getServiceDatabase()+ "?useSSL=false");
        basicDataSource.setDriverClassName(serviceRoute.getConnectionPool().getDriverClassName());
        basicDataSource.setInitialSize(serviceRoute.getConnectionPool().getInitialPoolSize());
        basicDataSource.setMaxTotal(serviceRoute.getConnectionPool().getMaxTotal());
        basicDataSource.setMaxIdle(serviceRoute.getConnectionPool().getMaxIdle());
        basicDataSource.setValidationQuery(serviceRoute.getConnectionPool().getValidationQuery());
        basicDataSource.setValidationQueryTimeout(serviceRoute.getConnectionPool().getValidationQueryTimeout());
        try {
            Class.forName(serviceRoute.getConnectionPool().getDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return basicDataSource;
    }
}
