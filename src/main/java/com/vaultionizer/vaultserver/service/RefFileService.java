package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.resource.RefFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

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

    public String readRefFile(Long refFileID){
        Set<String> refFileContents = refFileRepository.getRefFileContent(refFileID);
        if (refFileContents == null || refFileContents.size() != 1){
            return null;
        }
        return refFileContents.stream().findFirst().get();
    }

    public boolean updateRefFile(Long refFileID, String content){
        Set<RefFilesModel> models = refFileRepository.getRefFile(refFileID);
        if (models == null || models.size() != 1){
            return false;
        }
        RefFilesModel model = models.stream().findFirst().get();
        model.setContent(content);
        refFileRepository.save(model);
        return true;
    }

    public boolean hasNewVersion(Long refFileID, Timestamp lastFetched){
        return refFileRepository.checkNewVersion(refFileID, lastFetched) == 1;
    }

    public void deleteRefFile(Long refFileID){
        // TODO
    }
}
