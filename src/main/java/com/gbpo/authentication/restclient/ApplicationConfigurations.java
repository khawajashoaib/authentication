package com.gbpo.authentication.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Shoaib on 23/02/2018.
 */
@Configuration
@PropertySource("classpath:rest.properties")
public class ApplicationConfigurations {

  @Value("${auth.controller.context}")
  private String authController;

  @Value("${auth.adduser}")
  private String addUser;

  @Value("${auth.accountactivation}")
  private String accountActivation;

  public String getAuthController() {
    return authController;
  }

  public String addUser() {
    return String.format( "%s%s" ,getAuthController() ,addUser);
  }

  public String accountActivation() {
    return String.format( "%s%s" ,getAuthController() ,accountActivation);
  }

}
