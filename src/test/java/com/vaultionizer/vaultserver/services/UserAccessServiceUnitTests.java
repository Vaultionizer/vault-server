package com.vaultionizer.vaultserver.services;

import com.vaultionizer.vaultserver.resource.UserAccessRepository;
import com.vaultionizer.vaultserver.service.UserAccessService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UserAccessService")
class UserAccessServiceUnitTests {
    @MockBean
    private UserAccessRepository userAccessRepository;

    private UserAccessService userAccessService;

    @BeforeEach
    private void initialize() {
        userAccessRepository = Mockito.mock(UserAccessRepository.class);
        Mockito.when(userAccessRepository.hasAccess((long) 1, (long) 1)).thenReturn((long) 0);
        Mockito.when(userAccessRepository.hasAccess((long) 1, (long) 2)).thenReturn((long) 1);

        userAccessService = new UserAccessService(userAccessRepository);
    }

    @Test
    @DisplayName("Add user access.")
    void getUserIdOneResult() {
        userAccessService.addUserAccess((long) 1, (long) 1);
    }

    @Test
    @DisplayName("Check user access without access.")
    void checkUserAccessNoAccess() {
        Assertions.assertFalse(userAccessService.userHasAccess((long) 1, (long) 1));
    }

    @Test
    @DisplayName("Check user access.")
    void checkUserAccess() {
        Assertions.assertTrue(userAccessService.userHasAccess((long) 1, (long) 2));
    }

    @Test
    @DisplayName("Remove access although user has no access.")
    void removeAccessTestNoAccess() {
        Assertions.assertFalse(userAccessService.removeAccess((long) 1, (long) 1));
    }

    @Test
    @DisplayName("Remove access.")
    void removeAccessTest() {
        Assertions.assertTrue(userAccessService.removeAccess((long) 1, (long) 2));
    }
}