package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserService;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User Controller")
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SpaceService spaceService;

    private UserController userController;

    @BeforeEach
    private void initialize(){
        userService = Mockito.mock(UserService.class);
        spaceService = Mockito.mock(SpaceService.class);
        sessionService = Mockito.mock(SessionService.class);

        Mockito.when(userService.createUser(UserTestData.registerData[3].getUsername(), UserTestData.registerData[3].getKey()))
                .thenReturn(new UserModel(0L, "username", UserTestData.registerResponses[0].getSessionKey()));

        Mockito.when(userService.getUserIDCheckCredentials(UserTestData.loginUser[0].getUsername(), UserTestData.loginUser[0].getKey()))
                .thenReturn(-1L);
        Mockito.when(userService.getUserIDCheckCredentials(UserTestData.loginUser[1].getUsername(), UserTestData.loginUser[1].getKey()))
                .thenReturn(1L);

        Mockito.when(sessionService.addSession(0L))
                .thenReturn(new SessionModel(1L, 0L, UserTestData.registerResponses[0].getSessionKey(), "", null));

        Mockito.when(sessionService.addSession(1L))
                .thenReturn(new SessionModel(1L, "testSession"));

        userController = new UserController(userService, sessionService, spaceService);
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
        Assertions.assertEquals(((RegisterUserResponseDto)(Objects.requireNonNull(res.getBody()))).getUserID(), 0);
        Assertions.assertEquals(((RegisterUserResponseDto)(Objects.requireNonNull(res.getBody()))).getSessionKey(), "testSessionKey");
    }

    // testing login method
    @Test
    @DisplayName("Tests login with wrong key")
    public void loginUserWrongKey(){
        ResponseEntity<?> res = userController.loginUser(UserTestData.loginUser[0]);
        System.out.println(res);
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
