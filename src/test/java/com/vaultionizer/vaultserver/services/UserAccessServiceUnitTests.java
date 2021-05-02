package com.vaultionizer.vaultserver.services;

import com.vaultionizer.vaultserver.helpers.Hashing;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.resource.UserAccessRepository;
import com.vaultionizer.vaultserver.resource.UserRepository;
import com.vaultionizer.vaultserver.service.UserAccessService;
import com.vaultionizer.vaultserver.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UserAccessService")
public class UserAccessServiceUnitTests {
    @MockBean
    private UserAccessRepository userAccessRepository;

    private UserAccessService userAccessService;

    @BeforeEach
    private void initialize(){
        userAccessRepository = Mockito.mock(UserAccessRepository.class);
        Mockito.when(userAccessRepository.hasAccess((long)1, (long)1)).thenReturn((long)0);
        Mockito.when(userAccessRepository.hasAccess((long)1, (long)2)).thenReturn((long)1);

        userAccessService = new UserAccessService(userAccessRepository);
    }

    @Test
    @DisplayName("Add user access.")
    public void getUserIdOneResult(){
        userAccessService.addUserAccess((long)1, (long)1);
    }

    @Test
    @DisplayName("Check user access without access.")
    public void checkUserAccessNoAccess(){
        Assertions.assertFalse(userAccessService.userHasAccess((long)1, (long)1));
    }

    @Test
    @DisplayName("Check user access.")
    public void checkUserAccess(){
        Assertions.assertTrue(userAccessService.userHasAccess((long)1, (long)2));
    }

    @Test
    @DisplayName("Remove access although user has no access.")
    public void removeAccessTestNoAccess(){
        Assertions.assertFalse(userAccessService.removeAccess((long)1, (long)1));
    }
    @Test
    @DisplayName("Remove access.")
    public void removeAccessTest(){
        Assertions.assertTrue(userAccessService.removeAccess((long)1, (long)2));
    }
}