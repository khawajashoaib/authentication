package com.gbpo.authentication.service;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;

/**
 * Created by Shoaib on 16/02/2018.
 */
public interface UserAuthService {
    UserModel findByEmailAndCompanyId(String email, String companyId);
    String addUser(UserBO userModel);
}
