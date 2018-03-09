package com.gbpo.authentication.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.gbpo.authentication.bo.UserBO;
import com.gbpo.authentication.filter.SecurityConstants;
import com.gbpo.authentication.model.UserModel;
import com.gbpo.authentication.restclient.AuthRestClient;
import com.gbpo.authentication.service.UserAuthService;
import com.gbpo.authentication.util.CommonConstants;
import com.gbpo.authentication.util.CommonUtil;
import com.gbpo.authentication.util.JWTUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Shoaib on 16/02/2018.
 */
@RestController
@RequestMapping("/api")
@PropertySource("classpath:rest.properties")
public class UserController {

  public static final Logger logger = LogManager.getLogger(UserController.class);

  @Autowired
  private UserAuthService service;

  @Autowired
  private AuthRestClient restClient;

/*  @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
  public String helloUser() {
    logger.debug("hi");
    return "Hellow from Khawaja";
  }

  @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserModel> getUser() {
    return ResponseEntity.ok()
        .body(service.findByEmailAndCompanyId("admin", "2686"));
  }*/

  @PostMapping(path = "/adduser", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addUser(@Validated @RequestBody UserBO userModel,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      return ok()
          .header(String.valueOf(JWTUtil.createHeaders(request, response)))
          .body(service.addUser(userModel));
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/accountactivation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> activateAcct(@RequestParam("token") String token,
      HttpServletRequest httpServletRequest) {
    UserModel userModel = new UserModel();
    try {
      populateUserModel(userModel, token,JWTUtil.getUserNameFromRequest(JWTUtil.getTokenFromRequest(httpServletRequest)));
      return ResponseEntity.ok().body(service.validateUserAccount(userModel));
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> authenticationRequest(HttpServletRequest request) {
    try {
      return ResponseEntity.ok(service.refreshToken(request));
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }



  public void populateUserModel(UserModel model, String token, String userName) {
    Assert.notNull(token, CommonConstants.TOKEN_IS_NULL);
    Assert.notNull(userName, CommonConstants.USERNAME_IS_NULL);
    String[] appUserDetail = userName.split(",");
    model.setUrlToken(token);
    model.setEmail(appUserDetail[0].toString());
    model.setCompanyId(appUserDetail[1].toString());
  }


  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public String handleResourceNotFoundException(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    StringBuilder strBuilder = new StringBuilder();
    for (ConstraintViolation<?> violation : violations) {
      strBuilder.append(violation.getMessage() + "\n");
    }
    return strBuilder.toString();
  }

}
