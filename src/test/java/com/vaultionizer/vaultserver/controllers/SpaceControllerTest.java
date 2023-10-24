package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.SpaceTestData;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Space Controller")
class SpaceControllerTest {
    @MockBean
    private SessionService sessionService;

    @MockBean
    private SpaceService spaceService;

    @MockBean
    private RefFileService refFileService;

    @MockBean
    private UserAccessService userAccessService;

    @MockBean
    private PendingUploadService pendingUploadService;

    @MockBean
    private FileService fileService;


    private SpaceController spaceController;

    @BeforeEach
    private void initialize() {
        spaceService = Mockito.mock(SpaceService.class);
        sessionService = Mockito.mock(SessionService.class);
        refFileService = Mockito.mock(RefFileService.class);
        userAccessService = Mockito.mock(UserAccessService.class);
        fileService = Mockito.mock(FileService.class);
        pendingUploadService = Mockito.mock(PendingUploadService.class);


        Mockito.when(sessionService.getSession(
                SpaceTestData.createSpaceAuths[0].getUserID(),
                SpaceTestData.createSpaceAuths[0].getSessionKey())
        ).thenReturn(false);

        Mockito.when(sessionService.getSession(
                SpaceTestData.createSpaceAuths[1].getUserID(),
                SpaceTestData.createSpaceAuths[1].getSessionKey())
        ).thenReturn(true);

        Mockito.when(spaceService.checkSpaceCredentials(
                SpaceTestData.joinSpacesSpaceIDs[1],
                SpaceTestData.joinSpaces[1].getAuthKey())
        ).thenReturn(false);

        Mockito.when(spaceService.checkSpaceCredentials(
                SpaceTestData.joinSpacesSpaceIDs[2],
                SpaceTestData.joinSpaces[2].getAuthKey())
        ).thenReturn(true);

        Mockito.when(spaceService.getSpacesAccessible(SpaceTestData.getAllSpaces[0].getUserID()))
                .thenReturn(null);

        Mockito.when(sessionService.getSession(
                SpaceTestData.getAuthKeyCredentials[3].getUserID(),
                SpaceTestData.getAuthKeyCredentials[3].getSessionKey())
        ).thenReturn(true);


        Mockito.when(userAccessService.userHasAccess(
                SpaceTestData.getAuthKeyCredentials[1].getUserID(),
                SpaceTestData.getAuthKeysSpaceIds[1])
        ).thenReturn(false);

        Mockito.when(userAccessService.userHasAccess(
                SpaceTestData.getAuthKeyCredentials[2].getUserID(),
                SpaceTestData.getAuthKeysSpaceIds[2])
        ).thenReturn(true);
        Mockito.when(userAccessService.userHasAccess(
                SpaceTestData.getAuthKeyCredentials[3].getUserID(),
                SpaceTestData.getAuthKeysSpaceIds[3])
        ).thenReturn(true);

        Mockito.when(spaceService.createSpace(SpaceTestData.createSpaceAuths[1].getUserID(),
                SpaceTestData.createSpace[1].getReferenceFile(), SpaceTestData.createSpace[1].isPrivate(),
                false, false, SpaceTestData.createSpace[1].getAuthKey())
        ).thenReturn(1L);


        spaceController = new SpaceController(sessionService, spaceService, refFileService, pendingUploadService, fileService, userAccessService);
    }

    // Tests create space api
    @Test
    @DisplayName("Tests creating a new space using a wrong session key.")
    void createSpaceWrongSessionKey() {
        ResponseEntity<?> res = spaceController.createSpace(SpaceTestData.createSpace[0], SpaceTestData.createSpaceAuths[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests creating a new space using a correct session key.")
    void createSpace() {
        ResponseEntity<?> res = spaceController.createSpace(SpaceTestData.createSpace[1], SpaceTestData.createSpaceAuths[1]);
        Assertions.assertEquals(201, res.getStatusCodeValue());
        Assertions.assertEquals(1L, ((Long) (res.getBody())));
    }

    // Tests join space api
    @Test
    @DisplayName("Tests joining a space using a wrong session key.")
    void joinSpaceWrongSessionKey() {
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[0],
                SpaceTestData.joinSpacesAuth[0], SpaceTestData.joinSpacesSpaceIDs[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests joining a space using a correct session key but wrong authkey.")
    void joinSpaceWrongAuthKey() {
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[1],
                SpaceTestData.joinSpacesAuth[1], SpaceTestData.joinSpacesSpaceIDs[1]);
        Assertions.assertEquals(403, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests joining a space using a correct session key with correct authkey.")
    void joinSpace() {
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[2],
                SpaceTestData.joinSpacesAuth[2], SpaceTestData.joinSpacesSpaceIDs[2]);
        Assertions.assertEquals(200, res.getStatusCodeValue());
    }

    // Tests get all spaces
    @Test
    @DisplayName("Tests getting all space a user is part of using a wrong session key.")
    void getAllSpacesWrongSessionKey() {
        ResponseEntity<?> res = spaceController.getAllSpaces(SpaceTestData.getAllSpaces[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting all space a user is part of using a wrong session key.")
    void getAllSpaces() {
        ResponseEntity<?> res = spaceController.getAllSpaces(SpaceTestData.getAllSpaces[1]);
        Assertions.assertEquals(200, res.getStatusCodeValue());
        Assertions.assertNull(res.getBody());
    }

    // Tests get authentication key of a specified space
    @Test
    @DisplayName("Tests getting the authentication key of a space using a wrong session key.")
    void getAuthKeyWrongSessionKey() {
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeyCredentials[0], SpaceTestData.getAuthKeysSpaceIds[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting the authentication key of a space the user has no permission for.")
    void getAuthKeyWithoutPermission() {
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeyCredentials[1], SpaceTestData.getAuthKeysSpaceIds[1]);
        Assertions.assertEquals(403, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting the authentication key of a space the user access to.")
    void getAuthKey() {
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeyCredentials[3], SpaceTestData.getAuthKeysSpaceIds[3]);
        Assertions.assertEquals(406, res.getStatusCodeValue());
        Assertions.assertNull(res.getBody());
    }
}
