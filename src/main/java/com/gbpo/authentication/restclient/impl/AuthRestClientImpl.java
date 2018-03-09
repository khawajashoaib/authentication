package com.gbpo.authentication.restclient.impl;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.filter.SecurityConstants;
import com.gbpo.authentication.restclient.ApplicationConfigurations;
import com.gbpo.authentication.restclient.AuthRestClient;
import com.gbpo.authentication.util.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Shoaib on 23/02/2018.
 */
@Component
public class AuthRestClientImpl implements AuthRestClient {

  @Autowired
  private ApplicationConfigurations appProperty;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Override
  public String addUser(UserBO userModel, String token) throws Exception {
    Assert.notNull(userModel, CommonConstants.SOURCE_CLASS_NULL_OBJECT);
    Assert.notNull(token, CommonConstants.TOKEN_IS_NULL);
    String url = appProperty.addUser();
    String response = null;
    HttpEntity<UserBO> entity = new HttpEntity<>(userModel, createHeaders(token));
    ResponseEntity<String> responseEntity = restTemplate().exchange(url, HttpMethod.POST, entity, String.class);
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      response = responseEntity.getBody();
    }
    System.out.println("response to add User " + response);
    return response;
  }


  @Override
  public String activateUserAccount(String token) throws Exception {
    Assert.notNull(token, CommonConstants.TOKEN_IS_NULL);
    String url = appProperty.accountActivation();
    String response = null;
    HttpEntity<String> entity = new HttpEntity<>(token, createHeaders(token));
    ResponseEntity<String> responseEntity = restTemplate().exchange(url, HttpMethod.POST, entity, String.class);
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      response = responseEntity.getBody();
    }
    System.out.println("response to active User Account" + response);
    return response;
  }


  public HttpHeaders createHeaders(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(SecurityConstants.HEADER_STRING, token);
    return  headers;
  }

}
