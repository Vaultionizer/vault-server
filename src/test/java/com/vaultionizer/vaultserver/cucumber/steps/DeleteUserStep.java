package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.FileController;
import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Set;

public class DeleteUserStep extends Services {
    private Long userID;
    private Long spaceID;
    private String sessionKey;
    private boolean isPrivate;
    private String authKey;
    private ResponseEntity<?> res;
    private Long refFileID;

    @Autowired
    public DeleteUserStep(SpaceService spaceService, UserService userService,
                          UserAccessService userAccessService, SessionService sessionService,
                          RefFileService refFileService, PendingUploadService pendingUploadService,
                          FileService fileService, UserController userController,
                          SpaceController spaceController, SessionController sessionController,
                          FileController fileController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController, fileController);
    }

    @Given("the user created an account with name {string}")
    public void theUserCreatedAnAccountWithName(String username) {
        userID = userService.getUserIDCheckCredentials(username, UserTestData.registerData[3].getKey());
        sessionKey = sessionService.addSession(userID).getSessionKey();
    }

    @When("the user requests to delete the user")
    public void theUserRequestsToDeleteTheUser() {
        res = userController.deleteUser(new GenericAuthDto(userID, sessionKey));
    }

    @Then("the status code delete user is {int}")
    public void theStatusCodeDeleteUserIs(int status) throws Throwable {
        if (res.getStatusCodeValue() != status) throw new Throwable("Status code wrong: " + res.getStatusCodeValue());
    }

    @And("the user's spaces are deleted")
    public void theUserSSpacesAreDeleted() throws Throwable {
        var spaces = spaceService.getAllOwnedSpaces(userID);
        if (spaces.size() != 0)
            throw new Throwable("Not all own spaces deleted!: " + Arrays.toString(spaces.toArray()));
    }

    @And("the user has no access to any spaces")
    public void theUserHasNoAccessToAnySpaces() throws Throwable {
        Set<Long> accessible = userAccessService.getAllWithUser(userID);
        if (accessible.size() != 0)
            throw new Throwable("Not all accesses deleted!: " + Arrays.toString(accessible.toArray()));
    }
}
