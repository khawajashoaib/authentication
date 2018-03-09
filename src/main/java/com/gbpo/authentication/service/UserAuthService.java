package com.gbpo.authentication.service;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Shoaib on 16/02/2018.
 */
public interface UserAuthService {
    UserModel findByEmailAndCompanyId(String email, String companyId);
    String addUser(UserBO userModel);
    String validateUserAccount(UserModel model);
    UserModel findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(String email, String companyId, UserStatus userStatus);
    String refreshToken(HttpServletRequest request);
}
