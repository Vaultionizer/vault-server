package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.cucumber.steps.Services;
import com.vaultionizer.vaultserver.model.dto.CreateSpaceDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class CreateSpaceSteps extends Services {
    private Long userID;
    private Long spaceID;
    private String sessionKey;
    private boolean isPrivate;
    private String authKey;
    private ResponseEntity<?> res;

    @Autowired
    public CreateSpaceSteps(SpaceService spaceService, UserService userService,
                         UserAccessService userAccessService, SessionService sessionService,
                         RefFileService refFileService, PendingUploadService pendingUploadService,
                         FileService fileService, UserController userController,
                         SpaceController spaceController, SessionController sessionController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController);
    }

    @Given("the user is logged in with name {string}")
    public void the_user_is_logged_in(String username) {
        userID = this.userService.createUser(username, UserTestData.registerData[3].getKey());
        sessionKey = sessionService.addSession(userID).getSessionKey();
    }

    @And("the space should be private: {string}")
    public void theSpaceShouldBePrivateTrue(String isPrivate) {
        this.isPrivate = Boolean.parseBoolean(isPrivate);
    }

    @When("the user wants to create a space")
    public void theUserWantsToCreateASpace() {
        res = spaceController.createSpace(new CreateSpaceDto(new GenericAuthDto(userID, sessionKey), isPrivate, authKey, ""));
    }

    @Then("the status code of create space is {int}")
    public void theStatusCodeOfCreateSpaceIs(int status) throws Throwable{
        if (res.getStatusCode().value() != status) throw new Throwable(String.valueOf(res.getStatusCodeValue()));
    }

    @And("the returned ID is legitimate")
    public void theReturnedIDIsLegitimate() throws Throwable{
        spaceID = (Long)res.getBody();
        if (spaceID == null) throw new Throwable("No space created");
    }

    @And("the space is private")
    public void theSpaceIsPrivate() throws Throwable{
        if (!spaceService.getSpace(spaceID, userID).isPrivate()) throw new Throwable("Not private");
    }

    @And("the user has access")
    public void theUserHasAccess() throws Throwable{
        if(!userAccessService.userHasAccess(userID, spaceID)) throw new Throwable("Has no access");
    }

    @And("the space is shared")
    public void theSpaceIsShared() throws Throwable {
        if (spaceService.getSpace(spaceID, userID).isPrivate()) throw new Throwable("Not shared");
    }
}
