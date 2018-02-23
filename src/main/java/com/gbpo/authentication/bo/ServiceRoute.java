package com.gbpo.authentication.bo;

import lombok.Data;

/**
 * Created by Asad on 2/14/2018.
 */
@Data
public class ServiceRoute {
    private Long companyId;
    private String ip;
    private String port;
    private ConnectionPool connectionPool;

    public ServiceRoute withCompanyId(Long companyId){
        this.setCompanyId(companyId);
        return this;
    }

    public ServiceRoute withIp(String ip){
        this.setIp(ip);
        return this;
    }

    public ServiceRoute withPort(String port){
        this.setPort(port);
        return this;
    }

    public ServiceRoute withConnectionPool(ConnectionPool connectionPool){
        this.setConnectionPool(connectionPool);
        return this;
    }
    @Override
    public String toString() {
        return "[ServiceRoute companyId: " + companyId + ", ip: " + ip + ", port: " + port + ", connectionPool:[ " + connectionPool + " ]]";
    }

}
