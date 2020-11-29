package com.vaultionizer.vaultserver.cucumber.steps;

import com.vaultionizer.vaultserver.controllers.SessionController;
import com.vaultionizer.vaultserver.controllers.SpaceController;
import com.vaultionizer.vaultserver.controllers.UserController;
import com.vaultionizer.vaultserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;

public class Services {
    protected final SpaceService spaceService;
    protected final UserService userService;
    protected final UserAccessService userAccessService;
    protected final SessionService sessionService;
    protected final RefFileService refFileService;
    protected final PendingUploadService pendingUploadService;
    protected final FileService fileService;

    protected final UserController userController;
    protected final SpaceController spaceController;
    protected final SessionController sessionController;

    @Autowired
    public Services(SpaceService spaceService, UserService userService,
                    UserAccessService userAccessService, SessionService sessionService,
                    RefFileService refFileService, PendingUploadService pendingUploadService,
                    FileService fileService, UserController userController,
                    SpaceController spaceController, SessionController sessionController) {
        this.spaceService = spaceService;
        this.userService = userService;
        this.userAccessService = userAccessService;
        this.sessionService = sessionService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
        this.fileService = fileService;
        this.userController = userController;
        this.spaceController = spaceController;
        this.sessionController = sessionController;
    }
}
