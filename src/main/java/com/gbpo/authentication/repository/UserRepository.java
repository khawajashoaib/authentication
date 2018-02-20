package com.gbpo.authentication.repository;

import com.gbpo.authentication.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shoaib on 16/02/2018.
 */
@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByEmailAndCompanyId(String email, String companyId);
    UserModel findByEmailAndCompanyIdAndUserStatusAndArchivedFalse(String email, String companyId, String userStatus);
}
