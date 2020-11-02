package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;
import com.vaultionizer.vaultserver.resource.SessionRepository;
import com.vaultionizer.vaultserver.resource.UserRepository;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import org.apache.catalina.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User Controller")
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SpaceService spaceService;

    private UserController userController;

    @BeforeEach
    private void initialize(){
        userRepository = Mockito.mock(UserRepository.class);
        spaceService = Mockito.mock(SpaceService.class);
        sessionService = Mockito.mock(SessionService.class);

        Mockito.when(userRepository.save(any(UserModel.class)))
                .thenReturn(new UserModel(1L, UserTestData.registerResponses[0].getSessionKey()));
        Mockito.when(userRepository.checkCredentials(UserTestData.loginUser[0].getUserID(), UserTestData.loginUser[0].getKey()))
                .thenReturn(0L);
        Mockito.when(userRepository.checkCredentials(UserTestData.loginUser[1].getUserID(), UserTestData.loginUser[1].getKey()))
                .thenReturn(1L);

        Mockito.when(sessionService.addSession(1L))
                .thenReturn(new SessionModel(1L, 1L, UserTestData.registerResponses[0].getSessionKey(), null));

        Mockito.when(sessionService.addSession(2L))
                .thenReturn(new SessionModel(2L, "testSession"));

        userController = new UserController(userRepository, sessionService, spaceService);
    }


    // testing register controller
    @Test
    @DisplayName("Tests create user with key and ref file being null.")
    public void createUserKeyRefFileNull(){
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[0]);
        Assertions.assertEquals(res.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Tests create user with key and ref file being empty.")
    public void createUserKeyRefFileEmpty(){
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[1]);
        Assertions.assertEquals(res.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Tests create user with key and ref file being empty.")
    public void createUserKeyTooShort(){
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[2]);
        Assertions.assertEquals(res.getStatusCodeValue(), 400);
    }

    @Test
    @DisplayName("Tests create user with key matching the constraints.")
    public void createUser(){
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[3]);
        Assertions.assertEquals(res.getStatusCodeValue(), 201);
        Assertions.assertTrue(res.hasBody());
        Assertions.assertEquals(((RegisterUserResponseDto)(Objects.requireNonNull(res.getBody()))).getUserID(), 1);
        Assertions.assertEquals(((RegisterUserResponseDto)(Objects.requireNonNull(res.getBody()))).getSessionKey(), "testSessionKey");
    }

    // testing login method
    @Test
    @DisplayName("Tests login with wrong key")
    public void loginUserWrongKey(){
        ResponseEntity<?> res = userController.loginUser(UserTestData.loginUser[0]);
        Assertions.assertEquals(res.getStatusCodeValue(), 401);
    }

    @Test
    @DisplayName("Tests login with correct key")
    public void loginUser(){
        ResponseEntity<?> res = userController.loginUser(UserTestData.loginUser[1]);
        Assertions.assertEquals(res.getStatusCodeValue(), 200);
        Assertions.assertTrue(res.hasBody());
        Assertions.assertEquals(((LoginUserResponseDto)(Objects.requireNonNull(res.getBody()))).getSessionKey(), "testSession");
    }

}
