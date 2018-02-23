package com.gbpo.authentication.multidb;

/**
 * Created by Asad on 2/16/2018.
 */
public class ThreadLocalStorage {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCompanyId(Long companyId) {
        threadLocal.set(companyId);
    }

    public static Long getCompanyId() {
        return threadLocal.get();
    }

}
