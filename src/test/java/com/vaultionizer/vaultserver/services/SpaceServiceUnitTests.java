package com.vaultionizer.vaultserver.services;

import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.GetSpacesResponseDto;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import com.vaultionizer.vaultserver.service.RefFileService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserAccessService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SpaceService")
class SpaceServiceUnitTests {
    @MockBean
    private SpaceRepository spaceRepository;

    @MockBean
    private RefFileService refFileService;

    @MockBean
    private UserAccessService userAccessService;

    private SpaceService spaceService;

    private GetSpacesResponseDto resGetSpace = new GetSpacesResponseDto((long) 2, false, true, true, true);

    @BeforeEach
    private void initialize() {
        spaceRepository = Mockito.mock(SpaceRepository.class);
        refFileService = Mockito.mock(RefFileService.class);
        userAccessService = Mockito.mock(UserAccessService.class);

        Mockito.when(spaceRepository.findById((long) 1)).thenReturn(Optional.ofNullable(null));
        Mockito.when(spaceRepository.findById((long) 2)).thenReturn(Optional.of(new SpaceModel((long) 2, (long) 2, false, false, false, "")));
        Mockito.when(spaceRepository.save(Mockito.any())).thenReturn(new SpaceModel((long) 1, (long) 0, (long) 0, false, false, false, ""));
        spaceService = new SpaceService(spaceRepository, refFileService, userAccessService);
    }

    @Test
    @DisplayName("getSpace that does not exist.")
    void getSpaceNotExisting() {
        GetSpacesResponseDto res = spaceService.getSpace((long) 1, (long) 1);
        Assertions.assertNull(res);
    }

    @Test
    @DisplayName("getSpace.")
    void getSpace() {
        GetSpacesResponseDto res = spaceService.getSpace((long) 2, (long) 2);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(resGetSpace.getSpaceID(), res.getSpaceID());
        Assertions.assertEquals(resGetSpace.isCreator(), res.isCreator());
        Assertions.assertEquals(resGetSpace.isPrivate(), res.isPrivate());
    }


    @Test
    @DisplayName("Create space.")
    void createSpaceTest() {
        Assertions.assertEquals((long) 1, spaceService.createSpace((long) 1, "", true, false, false, ""));
    }


    @Test
    @DisplayName("Get spaces accessible.")
    void getSpacesAccess() {
        Assertions.assertEquals(0, spaceService.getSpacesAccessible((long) 1).size());
    }
}