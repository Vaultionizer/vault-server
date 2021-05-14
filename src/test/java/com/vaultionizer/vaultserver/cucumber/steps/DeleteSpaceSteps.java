package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.FileController;
import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.cucumber.steps.Services;
import com.vaultionizer.vaultserver.model.dto.AuthWrapperDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class DeleteSpaceSteps extends Services {
    private Long userID;
    private Long spaceID;
    private String sessionKey;
    private boolean isPrivate;
    private String authKey;
    private ResponseEntity<?> res;
    private Long refFileID;

    @Autowired
    public DeleteSpaceSteps(SpaceService spaceService, UserService userService,
                            UserAccessService userAccessService, SessionService sessionService,
                            RefFileService refFileService, PendingUploadService pendingUploadService,
                            FileService fileService, UserController userController,
                            SpaceController spaceController, SessionController sessionController,
                            FileController fileController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController, fileController);
    }


    @Given("the user is logged in properly")
    public void theUserIsLoggedInProperly() {
        userID = userService.getUserIDCheckCredentials("luigi", UserTestData.registerData[3].getKey());
        if ( userID == null){
            userID = userService.createUser("luigi", UserTestData.registerData[3].getKey());
        }
        sessionKey = sessionService.addSession(userID).getSessionKey();
    }

    @And("the user created the space")
    public void theUserCreatedTheSpace() {
        spaceID = spaceService.createSpace(userID, "", false, false, false, "authKey");
    }

    @When("the user wants to delete the space")
    public void theUserWantsToDeleteTheSpace() {
        refFileID = spaceService.getRefFileID(spaceID);
        res = spaceController.deleteSpace(spaceID, new GenericAuthDto(userID, sessionKey));
    }

    @Then("the response is {int}")
    public void theResponseIs(int status) throws Throwable{
        if (res.getStatusCodeValue() != status) throw new Throwable(String.valueOf(res.getStatusCodeValue()));
    }

    @And("the user has no access")
    public void theUserHasNoAccess() throws Throwable{
        if (userAccessService.userHasAccess(userID, spaceID)) throw new Throwable("Has access");
    }

    @And("there is no file in that space")
    public void thereIsNoFileInThatSpace() throws Throwable{
        if (fileService.countFilesInSpace(spaceID) != 0) throw new Throwable("Files were not deleted");
    }

    @And("all pending uploads are deleted")
    public void allPendingUploadsAreDeleted() throws Throwable{
        if (pendingUploadService.countPendingUploadsForSpace(spaceID) != 0) throw new Throwable("Not all uploads were deleted");
    }

    @And("the refFile was deleted")
    public void theRefFileWasDeleted() throws Throwable{
        if (refFileService.readRefFile(refFileID) != null) throw new Throwable("Ref file was not deleted");
    }

    @And("the space was deleted")
    public void theSpaceWasDeleted() throws Throwable{
        if (spaceService.getSpace(spaceID, userID) != null) throw new Throwable("Space was not deleted");
    }

    @And("another user created the space")
    public void anotherUserCreatedTheSpace() {
        spaceID = spaceService.createSpace(10000000L, "", false, false, false, "authKey");
    }

    @And("the user has access to the space")
    public void theUserHasAccessToTheSpace() {
        userAccessService.addUserAccess(spaceID, userID);
    }
}
