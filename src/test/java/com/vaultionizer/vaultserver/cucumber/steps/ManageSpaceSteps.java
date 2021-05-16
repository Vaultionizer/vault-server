package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.FileController;
import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.model.dto.*;
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
    private String DEFAULT_AUTH = "abc";

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
        spaceID = spaceService.createSpace(userID, "sd", false, true, true, DEFAULT_AUTH);
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
        res = spaceController.kickUsers(spaceID, getUserAuth(userID));
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
        res = spaceController.kickUsers(spaceID, getUserAuth(otherUserID));
    }

    @And("the other user still has access")
    public void theOtherUserStillHasAccess() throws Exception {
        if (!userAccessService.userHasAccess(otherUserID, spaceID))
            throw new Exception("Other user has no more access");
    }

    @And("the user creates a space that is {string}")
    public void theUserCreatesASpaceThatIs(String sharedState) {
        spaceID = spaceService.createSpace(userID, "sd", !parseSharedState(sharedState), true, true, DEFAULT_AUTH);

    }

    @When("the user sets the space {string}")
    public void theUserSetsTheSpace(String newSharedState) {
        res = spaceController.configureSpace(
                new ConfigureSpaceDto(true, true, !parseSharedState(newSharedState)), spaceID, getUserAuth(userID));
    }

    @When("the other user configures space")
    public void theOtherUserConfiguresSpace() {
        res = spaceController.configureSpace(
                new ConfigureSpaceDto(true, true, false), spaceID, getUserAuth(otherUserID));

    }

    private boolean parseSharedState(String state) {
        return state.equals("shared");
    }

    @And("the space is configured as {string}")
    public void theSpaceIsConfiguredAs(String sharedState) throws Exception {
        Boolean shared = spaceService.checkShared(spaceID);
        if (shared == null || shared == parseSharedState(sharedState))
            throw new Exception("Configuration failed. State is now " + shared);
    }

    @When("the user queries the config")
    public void theUserQueriesTheConfig() {
        res = spaceController.getSpaceConfig(spaceID, getUserAuth(userID));
    }

    @And("the config is correct.")
    public void theConfigIsCorrect() {
    }

    @When("the other user queries the config")
    public void theOtherUserQueriesTheConfig() {
        res = spaceController.getSpaceConfig(spaceID, getUserAuth(otherUserID));
    }

    @When("the user configures the space to write access {string} and invite {string}")
    public void theUserConfiguresTheSpaceToWriteAccessAndInvite(String writeAccess, String inviteAccess) {
        res = spaceController.configureSpace(new ConfigureSpaceDto(
                Boolean.parseBoolean(writeAccess), Boolean.parseBoolean(inviteAccess), true),
                spaceID, getUserAuth(userID));
    }

    @And("the config has write access {string} and invite {string}")
    public void theConfigHasWriteAccessAndInvite(String writeAccess, String inviteAccess) throws Exception {
        res = spaceController.getSpaceConfig(spaceID, getUserAuth(userID));
        if (res == null || !res.hasBody() || res.getBody() == null) throw new Exception("Querying config failed");
        var body = (GetSpaceConfigResponseDto)res.getBody();

        if (body.isUsersHaveWriteAccess() != Boolean.parseBoolean(writeAccess) || body.isUsersCanInvite() != Boolean.parseBoolean(inviteAccess))
            throw new Exception("Error. Wrong config. Write access: "+body.isUsersHaveWriteAccess()+"  auth key access: "+body.isUsersCanInvite());
    }

    @When("the other user configures the space")
    public void theOtherUserConfiguresTheSpace() {
        res = spaceController.configureSpace(new ConfigureSpaceDto(false, false, false), spaceID, getUserAuth(otherUserID));
    }

    private GenericAuthDto getUserAuth(Long _userID){
        var session = sessionService.addSession(_userID);
        return new GenericAuthDto(_userID, session.getSessionKey());
    }

    @When("the user changes the auth key to {string}")
    public void theUserChangesTheAuthKeyTo(String authKey) {
        res = spaceController.changeAuthKey(new ChangeAuthKeyDto(authKey), spaceID, getUserAuth(userID));
    }

    @And("the auth key is {string}")
    public void theAuthKeyIs(String authKey) throws Exception {
        checkAuthKey(authKey);
    }

    @When("the other user changes the auth key")
    public void theOtherUserChangesTheAuthKey() {
        res = spaceController.changeAuthKey(new ChangeAuthKeyDto("<Your ads here>"), spaceID, getUserAuth(otherUserID));
    }

    @And("the auth key remains unchanged")
    public void theAuthKeyRemainsUnchanged() throws Exception {
        checkAuthKey(DEFAULT_AUTH);
    }

    public void checkAuthKey(String expected) throws Exception {
        var result = spaceController.getAuthKey(getUserAuth(userID), spaceID);
        if (!result.hasBody() || result.getBody() == null)
            throw new Exception("Getting auth key failed. " + result.getStatusCode().value() + " -> "+result.getStatusCode().name());
        var auth = (SpaceAuthKeyResponseDto)result.getBody();
        if (auth == null || !auth.getAuthKey().equals(expected))
            throw new Exception("Auth key expected: "+ expected+ " is not actual: "+auth.getAuthKey());
    }
}
