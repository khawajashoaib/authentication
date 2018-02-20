package com.gbpo.authentication.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Shoaib on 16/02/2018.
 */

@Entity
@Table(name= "user")
@Data
@NoArgsConstructor
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="companyid")
    private String companyId;

    @Column(name="employeeId")
    private String employeeId;
    //@NotNull(message = "Email Cannot be null")
    private String email;
    //@NotNull(message = "Password Cannot be null")
    private String password;
    private String urlToken= "6465465454";

    @Column(name="tokenExpiry")
    private String tokenExpiry;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private  UserStatus userStatus = UserStatus.CREATED;
    private boolean archived = false;

}
