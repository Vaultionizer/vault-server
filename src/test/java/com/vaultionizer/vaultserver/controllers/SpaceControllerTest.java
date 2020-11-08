package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import com.vaultionizer.vaultserver.service.RefFileService;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserAccessService;
import com.vaultionizer.vaultserver.testdata.SpaceTestData;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Space Controller")
public class SpaceControllerTest {
    @MockBean
    private SpaceRepository spaceRepository;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SpaceService spaceService;

    @MockBean
    private RefFileService refFileService;

    @MockBean
    private UserAccessService userAccessService;

    private SpaceController spaceController;

    @BeforeEach
    private void initialize(){
        spaceRepository = Mockito.mock(SpaceRepository.class);
        spaceService = Mockito.mock(SpaceService.class);
        sessionService = Mockito.mock(SessionService.class);
        refFileService = Mockito.mock(RefFileService.class);
        userAccessService = Mockito.mock(UserAccessService.class);


        Mockito.when(sessionService.getSession(
                SpaceTestData.createSpace[0].getAuth().getUserID(),
                SpaceTestData.createSpace[0].getAuth().getSessionKey())
        ).thenReturn(false);

        Mockito.when(sessionService.getSession(
                SpaceTestData.createSpace[1].getAuth().getUserID(),
                SpaceTestData.createSpace[1].getAuth().getSessionKey())
        ).thenReturn(true);

        Mockito.when(refFileService.addNewRefFile(SpaceTestData.createSpace[1].getReferenceFile()))
                .thenReturn(new RefFilesModel(1L, ""));

        Mockito.when(spaceRepository.save(any(SpaceModel.class)))
                .thenReturn(new SpaceModel(1L, 1L, 1L, true, ""));

        Mockito.when(spaceService.checkSpaceCredentials(
                SpaceTestData.joinSpaces[1].getSpaceID(),
                SpaceTestData.joinSpaces[1].getAuthKey())
        ).thenReturn(false);

        Mockito.when(spaceService.checkSpaceCredentials(
                SpaceTestData.joinSpaces[2].getSpaceID(),
                SpaceTestData.joinSpaces[2].getAuthKey())
        ).thenReturn(true);

        Mockito.when(spaceService.getSpacesAccessible(SpaceTestData.getAllSpaces[0].getUserID()))
                .thenReturn(null);

        Mockito.when(userAccessService.userHasAccess(
                SpaceTestData.getAuthKeys[1].getAuth().getUserID(),
                SpaceTestData.getAuthKeys[1].getSpaceID())
        ).thenReturn(false);

        Mockito.when(userAccessService.userHasAccess(
                SpaceTestData.getAuthKeys[2].getAuth().getUserID(),
                SpaceTestData.getAuthKeys[2].getSpaceID())
        ).thenReturn(true);

        Mockito.when(spaceRepository.getSpaceAuthKey(SpaceTestData.getAuthKeys[2].getSpaceID()))
                .thenReturn(null);

        spaceController = new SpaceController(spaceRepository, sessionService, spaceService, refFileService, userAccessService);
    }

    // Tests create space api
    @Test
    @DisplayName("Tests creating a new space using a wrong session key.")
    public void createSpaceWrongSessionKey(){
        ResponseEntity<?> res = spaceController.createSpace(SpaceTestData.createSpace[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests creating a new space using a correct session key.")
    public void createSpace(){
        ResponseEntity<?> res = spaceController.createSpace(SpaceTestData.createSpace[1]);
        Assertions.assertEquals(201, res.getStatusCodeValue());
        Assertions.assertEquals(1L, ((Long)(res.getBody())));
    }

    // Tests join space api
    @Test
    @DisplayName("Tests joining a space using a wrong session key.")
    public void joinSpaceWrongSessionKey(){
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests joining a space using a correct session key but wrong authkey.")
    public void joinSpaceWrongAuthKey(){
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[1]);
        Assertions.assertEquals(403, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests joining a space using a correct session key with correct authkey.")
    public void joinSpace(){
        ResponseEntity<?> res = spaceController.joinSpace(SpaceTestData.joinSpaces[2]);
        Assertions.assertEquals(200, res.getStatusCodeValue());
    }

    // Tests get all spaces
    @Test
    @DisplayName("Tests getting all space a user is part of using a wrong session key.")
    public void getAllSpacesWrongSessionKey(){
        ResponseEntity<?> res = spaceController.getAllSpaces(SpaceTestData.getAllSpaces[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting all space a user is part of using a wrong session key.")
    public void getAllSpaces(){
        ResponseEntity<?> res = spaceController.getAllSpaces(SpaceTestData.getAllSpaces[1]);
        Assertions.assertEquals(200, res.getStatusCodeValue());
        Assertions.assertNull(res.getBody());
    }

    // Tests get authentication key of a specified space
    @Test
    @DisplayName("Tests getting the authentication key of a space using a wrong session key.")
    public void getAuthKeyWrongSessionKey(){
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeys[0]);
        Assertions.assertEquals(401, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting the authentication key of a space the user has no permission for.")
    public void getAuthKeyWithoutPermission(){
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeys[1]);
        Assertions.assertEquals(403, res.getStatusCodeValue());
    }

    @Test
    @DisplayName("Tests getting the authentication key of a space the user access to.")
    public void getAuthKey(){
        ResponseEntity<?> res = spaceController.getAuthKey(SpaceTestData.getAuthKeys[1]);
        Assertions.assertEquals(403, res.getStatusCodeValue());
        Assertions.assertNull(res.getBody());
    }
}
