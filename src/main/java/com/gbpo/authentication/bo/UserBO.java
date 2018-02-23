package com.gbpo.authentication.bo;

import com.gbpo.authentication.model.UserStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Created by Shoaib on 19/02/2018.
 */

@Component
@Data
@NoArgsConstructor
public class UserBO {

    private long id;
    @NotNull(message = "CompanyId Cannot be null")
    @NotEmpty
    private String companyId;
    @NotNull(message = "EmployeeId Cannot be null")
    @NotEmpty
    private String employeeId;
    @NotNull(message = "Email Cannot be null")
    @NotEmpty
    private String email;
    @NotNull(message = "Password Cannot be null")
    @NotEmpty
    private String password;
    private String urlToken= "6465465454";
    private String tokenExpiry;
    private UserStatus userStatus = UserStatus.CREATED;
    private boolean archived = false;
}
