package com.gbpo.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.gbpo.authentication.AuthenticationApplication;
import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.impl.UserAuthServiceImpl;
import com.gbpo.authentication.util.CommonConstants;
import com.gbpo.authentication.util.JWTUtil;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ExecutionException;
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
    userModel.setPassword("password");
    userModel.setCompanyId("123");
    userModel.setEmployeeId("3431213");
  }

  @Test
  public void findByEmailAndCompanyId_success() {

    given(userRepositoryMock.findByEmailAndCompanyId(anyString(), anyString()))
        .willReturn(userModel);
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
    UserBO user = createUserBObject();

    UserModel model = userServiceMock.customiseUser(user);
    Assert.assertNotNull(model.getTokenExpiry());
    Assert.assertNotNull(user.getUrlToken());
    assertEquals(model.getEmail(), user.getEmail());
    assertEquals(model.getCompanyId(), user.getCompanyId());
    assertEquals(model.getEmployeeId(), user.getEmployeeId());
    assertEquals(model.getPassword(), bCryptPasswordEncoder.encode(user.getPassword()));
  }

  @Test
  public void Test_activeUserAccount_success() {
    given(userRepositoryMock.save(userModel)).willReturn(userModel);
    String expectedResponse = userServiceMock.activeUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.ACCOUNT_ACTIVE_SUCCESS);
  }

  @Test(expected = Exception.class)
  public void Test_activeUserAccount_exception() {
    given(userRepositoryMock.save(userModel)).willThrow(new Exception());
    String expectedResponse = userServiceMock.activeUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.ACCOUT_ACTIVE_UNSUCCESSFUL);
  }

  @Test
  public void Test_isUserValid_success() {
    String userName = "shoaib,007";
    given(userServiceMock
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject())).willReturn(userModel);
    boolean result = userServiceMock.isUserValid(userName);
    assertEquals(result, true);
  }

  @Test
  public void Test_isUserValid_fail() {
    String userName = "shoaib,007";
    given(userServiceMock
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject())).willReturn(null);
    boolean result = userServiceMock.isUserValid(userName);
    assertEquals(result, false);
  }

  /*@Test
  public void Test_addUser_success() {

    doReturn(null).when(userRepositoryMock).findByEmailAndCompanyId(anyString(), anyString());
    given((userServiceMock.customiseUser(createUserBObject()))).willReturn(userModel);
    doReturn(userModel).when(userRepositoryMock).save(userModel);
    String expectedResponse = userServiceMock.addUser(createUserBObject());
    assertEquals(expectedResponse, "1");
  }*/

  @Test
  public void Test_addUser_fail() {

    doReturn(userModel).when(userRepositoryMock).findByEmailAndCompanyId(anyString(), anyString());
    String expectedResponse = userServiceMock.addUser(createUserBObject());
    assertEquals(expectedResponse, CommonConstants.USER_ALREADY_EXISTS);
  }

  @Test
  public void Test_validateUserAccount_success() {
    userModel.setTokenExpiry(new Timestamp(System.currentTimeMillis() + JWTUtil.ONE_DAY).toString());
    doReturn(userModel).when(userRepositoryMock)
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject());
    doReturn(userModel).when(userRepositoryMock).save(userModel);
    String expectedResponse = userServiceMock.validateUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.ACCOUNT_ACTIVE_SUCCESS);
  }

  @Test
  public void Test_validateUserAccount_whenUserNotFound_fail() {
    doReturn(null).when(userRepositoryMock)
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject());
    String expectedResponse = userServiceMock.validateUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.USER_NOT_FOUND);
  }

  @Test
  public void Test_validateUserAccount_whenTokenIsNotSame_fail() {
    UserModel userModelMock = new UserModel();
    userModelMock.setEmail("kahwaja");
    userModelMock.setPassword("shoaib");
    userModelMock.setUrlToken("4001158");
    doReturn(userModelMock).when(userRepositoryMock)
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject());
    String expectedResponse = userServiceMock.validateUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.UNMATCHED_TOKEN);
  }


  @Test
  public void Test_validateUserAccount_whenTokenExpiryIsNull_fail() {
    doReturn(userModel).when(userRepositoryMock)
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject());
    String expectedResponse = userServiceMock.validateUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.NULL_EXPIRY_DATE);
  }

  @Test
  public void Test_validateUserAccount_whenTokenIsExpired_fail() {
    userModel.setTokenExpiry("2018-02-26 14:58:18");
    doReturn(userModel).when(userRepositoryMock)
        .findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(anyString(), anyString(),
            anyObject());
    String expectedResponse = userServiceMock.validateUserAccount(userModel);
    assertEquals(expectedResponse, CommonConstants.EXPIRED_TOKEN);
  }

  public UserBO createUserBObject() {
    UserBO user = new UserBO();
    user.setId(1l);
    user.setEmail("khawaja");
    user.setPassword("password");
    user.setCompanyId("123");
    user.setEmployeeId("3431213");
    return user;
  }
}
