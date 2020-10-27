package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.resource.RefFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefFileService {
    private final RefFileRepository refFileRepository;

    @Autowired
    public RefFileService(RefFileRepository refFileRepository) {
        this.refFileRepository = refFileRepository;
    }

    public RefFilesModel addNewRefFile(String content){
        return refFileRepository.save(new RefFilesModel(content));
    }
}
