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

public class ManageSpaceSteps extends Services {
    private Long userID;
    private Long spaceID;
    private Long otherUserID;
    private ResponseEntity<?> res;

    @Autowired
    public ManageSpaceSteps(SpaceService spaceService, UserService userService,
                            UserAccessService userAccessService, SessionService sessionService,
                            RefFileService refFileService, PendingUploadService pendingUploadService,
                            FileService fileService, UserController userController,
                            SpaceController spaceController, SessionController sessionController,
                            FileController fileController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController, fileController);
    }

    @Then("the return code is {int}")
    public void theReturnCodeIs(int status) throws Exception {
        if (res.getStatusCode().value() != status) throw new Exception("Wrong status code.");
    }

    @Given("the user has created an account with name {string}")
    public void theUserHasCreatedAnAccountWithName(String name) {
        userID = userService.createUser(name, UserTestData.registerData[3].getKey());
        spaceID = spaceService.createSpace(userID, "sd", false, true, true, "abc");
    }

    @And("another user has an account with name {string}")
    public void anotherUserHasAnAccountWithName(String name) {
        otherUserID = userService.createUser(name, UserTestData.registerData[3].getKey());
    }

    @And("the other user has access")
    public void theOtherUserHasAccess() {
        userAccessService.addUserAccess(spaceID, otherUserID);
    }

    @When("the user kicks all users")
    public void theUserKicksAllUsers() {
        var session = sessionService.addSession(userID);
        res = spaceController.kickUsers(new GenericAuthDto(userID, session.getSessionKey()), spaceID);
    }

    @And("the other user has no access")
    public void theOtherUserHasNoAccess() {
        if (userAccessService.userHasAccess(otherUserID, spaceID)) userAccessService.removeAccess(otherUserID, spaceID);
    }

    @And("the user still has access")
    public void theUserStillHasAccess() throws Exception {
        if (!userAccessService.userHasAccess(userID, spaceID)) throw new Exception("User has no more access");
    }

    @When("the other user kicks all users")
    public void theOtherUserKicksAllUsers() {
        var session = sessionService.addSession(otherUserID);
        res = spaceController.kickUsers(new GenericAuthDto(otherUserID, session.getSessionKey()), spaceID);
    }

    @And("the other user still has access")
    public void theOtherUserStillHasAccess() throws Exception {
        if (!userAccessService.userHasAccess(otherUserID, spaceID)) throw new Exception("Other user has no more access");
    }
}
