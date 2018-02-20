package com.gbpo.authentication.rest;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.model.UserStatus;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Set;

/**
 * Created by Shoaib on 16/02/2018.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    public static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserAuthService service;
    @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String helloUser() {
        logger.debug("hi");
        return "Hellow from Khawaja";
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUser() {
        return ResponseEntity.ok()
                .body(service.findByEmailAndCompanyId("admin","2686"));
    }

    @PostMapping(path = "/adduser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@RequestBody UserBO userModel,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        try {
            return ResponseEntity.ok()
                    .header(String.valueOf(CommonUtil.createHeaders(request, response)))
                    .body(service.addUser(userModel));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return strBuilder.toString();
    }

}
