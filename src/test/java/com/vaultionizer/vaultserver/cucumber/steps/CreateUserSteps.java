package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.cucumber.steps.Services;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;
import com.vaultionizer.vaultserver.service.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class CreateUserSteps extends Services {
    private String username;
    private String key;
    private String refFileContent;

    private ResponseEntity<?> res;
    private LoginUserResponseDto responseDto;

    @Autowired
    public CreateUserSteps(SpaceService spaceService, UserService userService,
                           UserAccessService userAccessService, SessionService sessionService,
                           RefFileService refFileService, PendingUploadService pendingUploadService,
                           FileService fileService, UserController userController,
                           SpaceController spaceController, SessionController sessionController) {

        super(spaceService, userService, userAccessService, sessionService, refFileService,
                pendingUploadService, fileService, userController, spaceController, sessionController);
    }


    @And("the refFile is long enough")
    public void theRefFileIsLongEnough() {
        this.refFileContent = "PASTA";
    }

    @When("the client wants to register")
    public void theClientWantsToRegister() {
        res = userController.createUser(new RegisterUserDto(username, key, refFileContent));
    }


    @And("the user has a sessionKey")
    public void theUserHasASessionKey() throws Throwable{
        if (responseDto.getSessionKey() == null) throw new Throwable("No sessionKey");
    }

    @And("the user has a websocketToken")
    public void theUserHasAWebsocketToken() throws Throwable{
        if (responseDto.getWebsocketToken() == null) throw new Throwable("No webSocketToken...");
    }


    @Then("the status code of register is {int}")
    public void theStatusCodeOfRegisterIs(int status) throws Throwable {
        if (res.getStatusCode().value() != status) throw new Throwable(String.valueOf(res.getStatusCode().value()));
        responseDto = (LoginUserResponseDto)res.getBody();
    }

    @And("the user has a userID")
    public void theUserHasAUserID() throws Throwable{
        if (responseDto.getUserID() == null) throw new Throwable("No userID");
    }

    @Given("the username is {string}")
    public void theUsernameIs(String username) {
        this.username = username;
    }

    @And("the key is {string}")
    public void theKeyIs(String key) {
        this.key = key;
    }

    @And("the key is long enough")
    public void theKeyIsLongEnough() {
        this.key = "------------------------------------" +
                "------------------------------------------------------------"+
                "------------------------------------------------------------"+
                "------------------------------------------------------------";
    }

    @And("the refFile is {string}")
    public void theRefFileIs(String content) {
        this.refFileContent = content;
    }
}
