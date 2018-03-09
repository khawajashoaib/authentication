package com.gbpo.authentication.service;

import com.gbpo.authentication.AuthenticationApplication;
import com.gbpo.authentication.repository.UserRepository;
import com.gbpo.authentication.service.impl.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Shoaib on 02/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {AuthenticationApplication.class})
public class UserDetailsServiceImplTest {

  @Mock
  private UserRepository mockRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsMockService;

  @Before
  public void init() {

  }


  public void test_loadUserByUsername_success() {
  String userName = "kahwaja,007";
  //doReturn()
  userDetailsMockService.loadUserByUsername(userName);

  }

}
