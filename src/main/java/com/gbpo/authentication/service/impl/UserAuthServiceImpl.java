package com.gbpo.authentication.service.impl;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.dao.UserDao;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

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
        return repository.findByEmailAndCompanyId(email, companyId);
    }

    @Override
    public String addUser(UserBO userModel) {
        //check if username already exists
        if (this.findByEmailAndCompanyId(userModel.getEmail(), userModel.getCompanyId()) == null) {
            return String.valueOf(repository.save(customiseUser(userModel)).getId());
        }
        //if not
        return "UserName Already Exists";
    }

    public UserModel customiseUser(UserBO userObj) {
        userObj.setPassword(bCryptPasswordEncoder.encode(userObj.getPassword()));
        this.createUserToken(userObj);
        this.createTokenExpiry(userObj);
        return CommonUtil.mapSourceClassToDestination(userObj, UserModel.class);
    }

    public void createUserToken(UserBO userObj) {
        userObj.setUrlToken(String.format("%s%s",UUID.randomUUID().toString(), System.currentTimeMillis()));
    }

    public void createTokenExpiry(UserBO userObj) {
        userObj.setTokenExpiry(new Timestamp(System.currentTimeMillis() + CommonUtil.ONE_DAY).toString());
    }
}
