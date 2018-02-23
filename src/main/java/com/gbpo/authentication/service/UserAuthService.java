package com.gbpo.authentication.service;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;

/**
 * Created by Shoaib on 16/02/2018.
 */
public interface UserAuthService {
    UserModel findByEmailAndCompanyId(String email, String companyId);
    String addUser(UserBO userModel);
    String validateUserAccount(UserModel model);
    public UserModel findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(String email, String companyId);
}
