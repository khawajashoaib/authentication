package com.gbpo.authentication.rest;

/**
 * Created by Shoaib on 21/02/2018.
 */

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gbpo.authentication.AuthenticationApplication;
import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.filter.SecurityConstants;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={AuthenticationApplication.class })
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserAuthService userServiceMock;

  private UserBO userBO;

  @Before
  public void init() {
    userBO = new UserBO();
    userBO.setId(1l);
    userBO.setEmail("khawaja");
    userBO.setPassword("shoaib");
    userBO.setCompanyId("123");
    userBO.setEmployeeId("321");
  }


  @Test
  public void test_addUser_success() throws Exception {
    given(userServiceMock.addUser(userBO)).willReturn("1");
    String token = SecurityConstants.TOKEN_PREFIX + " " + JWTUtil.createToken("khawaja");
    mockMvc.perform(
            post("/api/adduser")
                .header("Content-Type", "application/json")
                .header(SecurityConstants.HEADER_STRING, token)
                .content(new ObjectMapper().writeValueAsString(userBO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
  }


  @Test
  public void test_addUser_fail_headerIsNull() throws Exception {
    when(userServiceMock.addUser(userBO)).thenReturn("1");
    mockMvc.perform(
        post("/api/adduser")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Content-Type", "application/json")
            .content(new ObjectMapper().writeValueAsString(userBO))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }


  @Test
  public void test_addUser_throwsException_UserIsNull() throws Exception {
    String token = SecurityConstants.TOKEN_PREFIX + " " + JWTUtil.createToken("khawaja");
    UserBO userBO = new UserBO();
    mockMvc.perform(
        post("/api/adduser")
            .header("Content-Type", "application/json")
            .header(SecurityConstants.HEADER_STRING, token)
            .content(new ObjectMapper().writeValueAsString(userBO))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }


}
