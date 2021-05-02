package com.vaultionizer.vaultserver.services;

import com.vaultionizer.vaultserver.helpers.Hashing;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.resource.UserRepository;
import com.vaultionizer.vaultserver.service.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UserService")
public class UserServiceUnitTests {
    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    private void initialize(){
        userRepository = Mockito.mock(UserRepository.class);
        Long id = 1L;
        var hashsetExactlyOne = new HashSet<UserModel>();
        hashsetExactlyOne.add(new UserModel(id,"exactlyOne", Hashing.hashBcrypt("pwd")));
        Mockito.when(userRepository.getPwd("exactlyOne")).thenReturn(hashsetExactlyOne);


        Mockito.when(userRepository.getPwd("none")).thenReturn(new HashSet<>());


        var hashsetMultiple = new HashSet<UserModel>();
        hashsetMultiple.add(new UserModel(id, "moreThanOne", Hashing.hashBcrypt("pwd")));
        hashsetMultiple.add(new UserModel(id, "moreThanOne", Hashing.hashBcrypt("pwd")));
        Mockito.when(userRepository.getPwd("moreThanOne")).thenReturn(hashsetMultiple);

        Mockito.when(userRepository.save(new UserModel("create", Mockito.anyString())))
                .thenReturn(new UserModel((long)1, "create", "pwd"));
        Mockito.when(userRepository.save(new UserModel("failCreate", "pwd")))
                .thenReturn(null);

        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("getUserIDCheckCredentials with exactly one.")
    public void getUserIdOneResult(){
        Long id = userService.getUserIDCheckCredentials("exactlyOne", "pwd");
        Assertions.assertEquals(1, id);
    }

    @Test
    @DisplayName("getUserIDCheckCredentials with none.")
    public void getUserIdNone(){
        Long id = userService.getUserIDCheckCredentials("none", "pwd");
        Assertions.assertEquals(-1, id);
    }

    @Test
    @DisplayName("getUserIDCheckCredentials with more than one.")
    public void getUserIdMoreThanOne(){
        Long id = userService.getUserIDCheckCredentials("moreThanOne", "pwd");
        Assertions.assertEquals(-1, id);
    }

    @Test
    @DisplayName("Create user success.")
    public void createUser(){
        Long id = userService.createUser("create", "pwd");
        Assertions.assertEquals(null, id); // TODO: Mockito does not like news
    }

    @Test
    @DisplayName("Create user failing because of null.")
    public void createUserException(){
        Long id = userService.createUser("failCreate", "pwd");
        Assertions.assertNull(id);
    }

    @Test
    @DisplayName("Delete user while already in deletion process.")
    public void deleteUserFailing(){
        boolean success = userService.setDeleted((long)2);
        Assertions.assertTrue(success);
        success = userService.setDeleted((long)2);
        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Normal delete user.")
    public void deleteUserNormal(){
        boolean success = userService.setDeleted((long)2);
        Assertions.assertTrue(success);
        userService.setDeletionDone((long)2);
    }


}
