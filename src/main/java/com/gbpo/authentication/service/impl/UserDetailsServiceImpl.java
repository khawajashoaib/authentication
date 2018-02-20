package com.gbpo.authentication.service.impl;

import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

/**
 * Created by Shoaib on 16/02/2018.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String[] appUserDetail = username.split(",");
        String email = appUserDetail[0].toString();
        String companyId = appUserDetail[1].toString();
        //System.out.println("username is : " + userName+  "CompanyId is : "+  companyId);
        UserModel applicationUser = userRepository.findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(email, companyId, UserStatus.ACTIVE.toString());
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getEmployeeId(), applicationUser.getPassword(), emptyList());
    }
}
