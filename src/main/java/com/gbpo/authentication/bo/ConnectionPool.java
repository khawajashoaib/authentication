package com.gbpo.authentication.bo;

import javax.persistence.Embeddable;
import lombok.Data;

/**
 * Created by Asad on 2/16/2018.
 */
@Data
@Embeddable
public class ConnectionPool {
    private String username;
    private String password;
    private int maxIdle;
    private int maxTotal;
    private String driverClassName;
    private int initialPoolSize;
    private String validationQuery;
    private int validationQueryTimeout;
    @Override
    public String toString() {
        return "[ConnectionPool username: " + username + ", maxIdle: " + maxIdle + ", maxTotal: " + maxTotal + ","
            + " driverClassName: " + driverClassName + ",  initialPoolSize: " + initialPoolSize + ", validationQuery: " + validationQuery + ","
            + " validationQueryTimeout: " + validationQueryTimeout + "]";
    }
}
