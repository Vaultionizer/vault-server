package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.resource.RefFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Long requestUploadFiles(Long refFileID, Long amountValues){
        Optional<RefFilesModel> model = this.refFileRepository.findById(refFileID);
        if (model.isEmpty()) return -1L;
        RefFilesModel m = model.get();
        Long saveIndex = m.getSaveIndex();
        m.addToSaveIndex(amountValues);
        this.refFileRepository.save(m);
        return saveIndex;
    }
}
