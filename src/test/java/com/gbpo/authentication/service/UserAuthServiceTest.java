package com.gbpo.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.gbpo.authentication.AuthenticationApplication;
import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.impl.UserAuthServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Shoaib on 22/02/2018.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {AuthenticationApplication.class})
public class UserAuthServiceTest {

  @Mock
  private UserRepository userRepositoryMock;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private UserAuthServiceImpl userServiceMock;
  private UserModel userModel;

  @Before
  public void init() {
    userModel = new UserModel();
    userModel.setId(1l);
    userModel.setEmail("khawaja");
    userModel.setPassword("shoaib");
    userModel.setCompanyId("123");
    userModel.setEmployeeId("3431213");
  }

  @Test
  public void findByEmailAndCompanyId_success() {

    given(userRepositoryMock.findByEmailAndCompanyId(anyString(), anyString())).willReturn(userModel);
    assertEquals(userModel, userServiceMock.findByEmailAndCompanyId(anyString(), anyString()));
  }

  @Test
  public void createTokenExpiry() {
    UserBO user = new UserBO();
    user.setEmail("khawaja");
    userServiceMock.createTokenExpiry(user);
    Assert.assertNotNull(user.getTokenExpiry());
  }

  @Test
  public void createUserToken() {
    UserBO user = new UserBO();
    user.setEmail("khawaja");
    userServiceMock.createUserToken(user);
    Assert.assertNotNull(user.getUrlToken());
  }

  @Test
  public void customiseUser_success() {
    UserBO user = new UserBO();
    user.setEmail("khawaja");
    user.setPassword("password");
    user.setCompanyId("123");
    user.setEmployeeId("3431213");

    UserModel model = userServiceMock.customiseUser(user);
    Assert.assertNotNull(model.getTokenExpiry());
    Assert.assertNotNull(user.getUrlToken());
    assertEquals(model.getEmail(), user.getEmail());
    assertEquals(model.getCompanyId(), user.getCompanyId());
    assertEquals(model.getEmployeeId(), user.getEmployeeId());
    assertEquals(model.getPassword(), bCryptPasswordEncoder.encode(user.getPassword()));
  }
}
