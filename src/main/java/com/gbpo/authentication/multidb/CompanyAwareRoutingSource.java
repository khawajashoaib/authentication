package com.gbpo.authentication.multidb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by Asad on 2/16/2018.
 */
public class CompanyAwareRoutingSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalStorage.getCompanyId();
    }
}
