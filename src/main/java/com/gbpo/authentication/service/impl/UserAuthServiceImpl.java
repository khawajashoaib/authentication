package com.gbpo.authentication.service.impl;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.CommonConstants;
import com.gbpo.authentication.util.CommonUtil;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
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
    //check if username already exists
    if (this.findByEmailAndCompanyId(userModel.getEmail(), userModel.getCompanyId()) == null) {
      return String.valueOf(repository.save(customiseUser(userModel)).getId());
    }
    //if not
    return CommonConstants.USER_ALREADY_EXISTS;
  }

  @Override
  public String validateUserAccount(UserModel model) {
    Assert.notNull(model.getEmail(), CommonConstants.NULL_EMAIL);
    Assert.notNull(model.getCompanyId(), CommonConstants.NULL_COMPANYID);
    String response = CommonConstants.USER_NOT_FOUND;
    try {
      UserModel userModelDb = findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(model.getEmail(),
          model.getCompanyId());
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
      String companyId) {
    Assert.notNull(email, CommonConstants.NULL_EMAIL);
    Assert.notNull(companyId, CommonConstants.NULL_COMPANYID);
    return repository.findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(email, companyId,
        UserStatus.CREATED);
  }

  public String checkTokenExpiryAndActiveUser(UserModel userModel) {
    Assert.notNull(userModel.getTokenExpiry(), CommonConstants.NULl_EXPIRY_DATE);
    if (Timestamp.valueOf(userModel.getTokenExpiry()).compareTo(new Date()) == 1) {
      userModel.setUserStatus(UserStatus.ACTIVE);
      return activeUserAccount(userModel);
    }
    return CommonConstants.TOKEN_EXPIRE;
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
        .setTokenExpiry(new Timestamp(System.currentTimeMillis() + CommonUtil.ONE_DAY).toString());
  }
}
