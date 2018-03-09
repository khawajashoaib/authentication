package com.gbpo.authentication.restclient;

import com.gbpo.authentication.bo.UserBO;

/**
 * Created by Shoaib on 23/02/2018.
 */
public interface AuthRestClient {

  public String addUser(UserBO userModel, String token) throws Exception;
  public String activateUserAccount(String token) throws Exception;
}
