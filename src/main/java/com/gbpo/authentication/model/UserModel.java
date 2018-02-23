package com.gbpo.authentication.model;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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
