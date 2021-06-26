package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User Controller")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SpaceService spaceService;

    @MockBean
    private UserAccessService userAccessService;

    @MockBean
    private PendingUploadService pendingUploadService;


    @MockBean
    private SpaceController spaceController;

    private UserController userController;

    @BeforeEach
    private void initialize() {
        userService = Mockito.mock(UserService.class);
        spaceService = Mockito.mock(SpaceService.class);
        sessionService = Mockito.mock(SessionService.class);
        userAccessService = Mockito.mock(UserAccessService.class);
        pendingUploadService = Mockito.mock(PendingUploadService.class);
        spaceController = Mockito.mock(SpaceController.class);

        Mockito.when(userService.createUser(UserTestData.registerData[3].getUsername(), UserTestData.registerData[3].getKey()))
                .thenReturn(0L);

        Mockito.when(userService.getUserIDCheckCredentials(UserTestData.loginUser[0].getUsername(), UserTestData.loginUser[0].getKey()))
                .thenReturn(-1L);
        Mockito.when(userService.getUserIDCheckCredentials(UserTestData.loginUser[1].getUsername(), UserTestData.loginUser[1].getKey()))
                .thenReturn(1L);

        Mockito.when(sessionService.addSession(0L))
                .thenReturn(new LoginUserResponseDto(0L, UserTestData.registerResponses[0].getSessionKey(), ""));

        Mockito.when(sessionService.addSession(1L))
                .thenReturn(new LoginUserResponseDto(1L, "testSession", "testWebsocket"));

        userController = new UserController(userService, sessionService, spaceService, spaceController, userAccessService, pendingUploadService);
    }


    // testing register controller
    @Test
    @DisplayName("Tests create user with key and ref file being null.")
    void createUserKeyRefFileNull() {
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[0]);
        Assertions.assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests create user with key and ref file being empty.")
    void createUserKeyRefFileEmpty() {
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[1]);
        Assertions.assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests create user with key and ref file being empty.")
    void createUserKeyTooShort() {
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[2]);
        Assertions.assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests create user with key matching the constraints.")
    void createUser() {
        ResponseEntity<?> res = userController.createUser(UserTestData.registerData[3]);
        Assertions.assertEquals(201, res.getStatusCodeValue());
        Assertions.assertTrue(res.hasBody());
        Assertions.assertEquals(0, ((LoginUserResponseDto) (Objects.requireNonNull(res.getBody()))).getUserID());
        Assertions.assertEquals("testSessionKey", ((LoginUserResponseDto) (Objects.requireNonNull(res.getBody()))).getSessionKey());
    }

    // testing login method
    @Test
    @DisplayName("Tests login with wrong key")
    void loginUserWrongKey() {
        ResponseEntity<?> res = userController.loginUser(UserTestData.loginUser[0]);
        System.out.println(res);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests login with correct key")
    void loginUser() {
        ResponseEntity<?> res = userController.loginUser(UserTestData.loginUser[1]);
        Assertions.assertEquals(200, res.getStatusCodeValue());
        Assertions.assertTrue(res.hasBody());
        Assertions.assertEquals("testSession", ((LoginUserResponseDto) (Objects.requireNonNull(res.getBody()))).getSessionKey());
    }

}
