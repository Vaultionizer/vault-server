package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.FileController;
import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.*;
import com.vaultionizer.vaultserver.testdata.UserTestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.File;

public class DownloadFileSteps extends Services {
    private Long userID;
    private Long spaceID;
    private String sessionKey;
    private boolean isPrivate;
    private String authKey;
    private ResponseEntity<?> res;

    @Autowired
    public DownloadFileSteps(SpaceService spaceService, UserService userService,
                             UserAccessService userAccessService, SessionService sessionService,
                             RefFileService refFileService, PendingUploadService pendingUploadService,
                             FileService fileService, UserController userController,
                             SpaceController spaceController, SessionController sessionController,
                             FileController fileController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController, fileController);
    }

    @Given("the user has successfully created an account with username {string}")
    public void theUserHasSuccessfullyCreatedAnAccountWithUsername(String username) {
        userID = this.userService.createUser(username, UserTestData.registerData[3].getKey());
        spaceID = spaceService.createSpace(userID, "Genki-DAMA", false, false, false, "broly");
        sessionKey = sessionService.addSession(userID).getSessionKey();
    }

    @And("the file with saveIndex {long} was uploaded")
    public void theFileWithSaveIndexWasUploaded(Long saveIndex) {
        File dir = new File("trash/cucumberTestAssets/");
        dir.mkdirs();
        Config.SPACE_PATH = (dir.getAbsolutePath() + "/");
        fileService.setUploadFile(spaceID, saveIndex);
        fileService.writeToFile("Kame-hame-HAAAA", spaceID, saveIndex);
    }

    @When("the user requests to download the file with saveIndex {long}")
    public void theUserRequestsToDownloadTheFileWithSaveIndex(Long saveIndex) {
        res = fileController.downloadFile(new GenericAuthDto(userID, sessionKey), spaceID, saveIndex);
    }

    @Then("the status code of download is {int}")
    public void theStatusCodeOfDownloadIs(int status) throws Throwable {
        if (res.getStatusCodeValue() != status) throw new Throwable("Wrong status code: " + res.getStatusCodeValue());
    }

    @And("the space id is set inappropriately")
    public void theSpaceIdIsSetInappropriately() {
        spaceID *= 2; // just some other value of a non-existing space
    }

    @And("the file with saveIndex {long} is being modified")
    public void theFileWithSaveIndexIsBeingModified(Long saveIndex) {
        fileService.setModified(spaceID, saveIndex);
    }
}
