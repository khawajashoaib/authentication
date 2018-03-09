package com.gbpo.authentication.service.impl;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.CommonConstants;
import com.gbpo.authentication.util.CommonUtil;
import com.gbpo.authentication.util.JWTUtil;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by Shoaib on 16/02/2018.
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserModel findByEmailAndCompanyId(String email, String companyId) {
    Assert.notNull(email, CommonConstants.NULL_EMAIL);
    Assert.notNull(companyId, CommonConstants.NULL_COMPANYID);
    try {
      return repository.findByEmailAndCompanyId(email, companyId);
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  @Override
  public String addUser(UserBO userModel) {
    Assert.notNull(userModel.getEmail(), CommonConstants.NULL_EMAIL);
    Assert.notNull(userModel.getCompanyId(), CommonConstants.NULL_COMPANYID);
    if (findByEmailAndCompanyId(userModel.getEmail(), userModel.getCompanyId()) == null) {
      UserModel model = repository.save(customiseUser(userModel));
      return String.valueOf(model.getId());
    }
    return CommonConstants.USER_ALREADY_EXISTS;
  }

  @Override
  public String validateUserAccount(UserModel model) {
    Assert.notNull(model.getEmail(), CommonConstants.NULL_EMAIL);
    Assert.notNull(model.getCompanyId(), CommonConstants.NULL_COMPANYID);
    String response = CommonConstants.USER_NOT_FOUND;
    try {
      UserModel userModelDb = findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(model.getEmail(),
          model.getCompanyId(), UserStatus.CREATED);
      Assert.notNull(userModelDb, response);
      if (userModelDb.getUrlToken().equals(model.getUrlToken())) {
        response = checkTokenExpiryAndActiveUser(userModelDb);
      } else {
        response = CommonConstants.UNMATCHED_TOKEN;
      }
    } catch (Exception e) {
      System.out.println(e);
      response = e.getMessage();
    }
    return response;
  }

  @Override
  public UserModel findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(String email,
      String companyId, UserStatus userStatus) {
    Assert.notNull(email, CommonConstants.NULL_EMAIL);
    Assert.notNull(companyId, CommonConstants.NULL_COMPANYID);
    return repository.findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(email, companyId,
        userStatus);
  }

  @Override
  public String refreshToken(HttpServletRequest request) {
    String token = JWTUtil.getTokenFromRequest(request);
    if (!isUserValid(JWTUtil.getUserNameFromRequest(token))) {
      return CommonConstants.USER_NOT_FOUND;
    }
    return refreshToken(token);
  }

  public String refreshToken(String token) {
    if (JWTUtil.checkTokenTimeValidity(token)) {
      return JWTUtil.createRefreshToken(token);
    }
    return CommonConstants.EXPIRED_TOKEN;
  }


  public boolean isUserValid(String userName) {
    Assert.notNull(userName, CommonConstants.USERNAME_IS_NULL);
    String[] userNameArray = userName.split(",");
    String email = userNameArray[0];
    String companyId = userNameArray[1];
    return
        (findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(email, companyId, UserStatus.ACTIVE)
            != null) ? true : false;
  }

  public String checkTokenExpiryAndActiveUser(UserModel userModel) {
    Assert.notNull(userModel.getTokenExpiry(), CommonConstants.NULL_EXPIRY_DATE);
    if (Timestamp.valueOf(userModel.getTokenExpiry()).compareTo(new Date()) == 1) {
      userModel.setUserStatus(UserStatus.ACTIVE);
      return activeUserAccount(userModel);
    }
    return CommonConstants.EXPIRED_TOKEN;
  }

  public String activeUserAccount(UserModel userModel) {
    try {
      repository.save(userModel);
      return CommonConstants.ACCOUNT_ACTIVE_SUCCESS;
    } catch (Exception e) {
      System.out.println(e);
    }
    return CommonConstants.ACCOUT_ACTIVE_UNSUCCESSFUL;
  }

  public UserModel customiseUser(UserBO userObj) {
    Assert.notNull(userObj.getPassword(), CommonConstants.NULL_PASSWORD);
    userObj.setPassword(bCryptPasswordEncoder.encode(userObj.getPassword()));
    this.createUserToken(userObj);
    this.createTokenExpiry(userObj);
    return CommonUtil.mapSourceClassToDestination(userObj, UserModel.class);
  }

  public void createUserToken(UserBO userObj) {
    userObj.setUrlToken(
        String.format("%s%s", UUID.randomUUID().toString(), System.currentTimeMillis()));
  }

  public void createTokenExpiry(UserBO userObj) {
    userObj
        .setTokenExpiry(new Timestamp(System.currentTimeMillis() + JWTUtil.ONE_DAY).toString());
  }
}
