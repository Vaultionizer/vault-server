package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.FileController;
import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.model.dto.FileUploadDto;
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

public class UploadFileSteps extends Services {
    private Long userID;
    private String sessionKey;
    private Long spaceID;

    private ResponseEntity<?> res;

    @Autowired
    public UploadFileSteps(SpaceService spaceService, UserService userService,
                           UserAccessService userAccessService, SessionService sessionService,
                           RefFileService refFileService, PendingUploadService pendingUploadService,
                           FileService fileService, UserController userController,
                           SpaceController spaceController, SessionController sessionController,
                           FileController fileController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController, fileController);
    }

    @Given("the user has an account with name {string}")
    public void theUserHasAnAccountWithName(String username) {
        userID = this.userService.createUser(username, UserTestData.registerData[3].getKey());
        sessionKey = sessionService.addSession(userID).getSessionKey();
        spaceID = spaceService.createSpace(userID, "NANI", false, false, false, "dbz");
    }

    @When("the user requests to upload {int} files")
    public void theUserRequestsToUploadFiles(int amount) {
        res = fileController.uploadFiles(new FileUploadDto(new GenericAuthDto(userID, sessionKey), spaceID, amount));
    }

    @Then("the status code of upload is {int}")
    public void theStatusCodeOfUploadIs(int status) throws Throwable{
        if (res.getStatusCodeValue() != status) throw new Throwable("Status code wrong: "+ res.getStatusCodeValue());
    }

    @And("the saveIndex is {long}")
    public void theSaveIndexIs(Long saveIndex) throws Throwable{
        if (res.getBody() != saveIndex) throw new Throwable("Wrong body (saveIndex): "+ res.getBody());
    }

    @And("the spaceID is {long}")
    public void theSpaceIDIs(Long spaceID) {
        this.spaceID = spaceID;
    }

    @And("the space is not accessible")
    public void theSpaceIsNotAccessible() {
        userAccessService.removeAccess(userID, spaceID);
    }

    @And("the reffile does not exist")
    public void theReffileDoesNotExist() {
        Long refFileID = spaceService.getRefFileID(spaceID);
        refFileService.deleteRefFile(refFileID);
    }
}
