package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.helpers.FileStatus;
import com.vaultionizer.vaultserver.model.db.FileModel;
import com.vaultionizer.vaultserver.resource.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;

@Service
public class FileService {
    private final HashMap<Long, Integer> readMap; // needed to allow for multiple simultaneous reads
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.readMap = new HashMap<>();
    }

    public boolean setUploadFile(Long spaceID, Long saveIndex){
        FileModel model = findFile(spaceID, saveIndex);
        if (model != null) return false;

        model = new FileModel(spaceID, saveIndex);
        fileRepository.save(model);
        return true;
    }

    public FileStatus setDownloadFile(Long spaceID, Long saveIndex){
        FileModel model = findFile(spaceID, saveIndex);
        if (model == null) return null;
        switch (model.getStatus()){
            case ACCESSIBLE:
                model.setStatus(FileStatus.READ_FROM);
                fileRepository.save(model);
            case READ_FROM:
                Integer amount = readMap.get(model.getFileID());
                if (amount == null) amount = 0;
                amount++;
                readMap.put(model.getFileID(), amount);
                return FileStatus.READ_FROM;
            default:
                return model.getStatus();
        }
    }

    public boolean writeToFile(String content, Long spaceID, Long saveIndex){
        FileModel model = findFile(spaceID, saveIndex);
        if (model == null || model.getStatus() != FileStatus.UPLOADING) return false;
        File f = new File(getFilePath(spaceID, saveIndex));
        if (!f.exists()){
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(f, false))){
            writer.print(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        model.setStatus(FileStatus.ACCESSIBLE);
        fileRepository.save(model);
        return true;
    }

    public String makeDownload(Long spaceID, Long saveIndex){
        FileModel fileModel = findFile(spaceID, saveIndex);
        if (fileModel == null) return null;
        if (readMap.get(fileModel.getFileID()) == 0
                || readMap.get(fileModel.getFileID()) == null
                || fileModel.getStatus() != FileStatus.READ_FROM) return null;
        String content = readFromFile(spaceID, saveIndex);
        if (readMap.get(fileModel.getFileID()) == 1){
            readMap.remove(fileModel.getFileID());
            fileModel.setStatus(FileStatus.ACCESSIBLE);
        }
        else{
            Integer amount = readMap.get(fileModel.getFileID());
            if (amount <= 0 || amount == null) return null;
            amount--;
            readMap.put(fileModel.getFileID(), amount);
        }
        return content;
    }

    public String readFromFile(Long spaceID, Long saveIndex){
        File f = new File(getFilePath(spaceID, saveIndex));
        if (!f.exists()) return null;

        StringBuilder builder = new StringBuilder();
        try{
            return Files.readString(f.toPath().toAbsolutePath());
        } catch (IOException e) {
            return null;
        }
    }

    private String getFilePath(Long spaceID, Long saveIndex){
        return Config.SPACE_PATH +spaceID+"/"+saveIndex+".bin";
    }

    private synchronized FileModel findFile(Long spaceID, Long saveIndex){
        return fileRepository.findFile(spaceID, saveIndex);
    }

    public boolean deleteFile(Long spaceID, Long saveIndex){
        FileModel file = fileRepository.findFile(spaceID, saveIndex);
        if (file == null) return true;
        switch (file.getStatus()){
            case ACCESSIBLE:
            case READ_FROM:
                fileRepository.delete(file);
                return removeFileFromDisk(spaceID, saveIndex);
            default:
                return false;
        }
    }

    private boolean removeFileFromDisk(Long spaceID, Long saveIndex){
        File file = new File(getFilePath(spaceID, saveIndex));
        if (file.exists()) return file.delete();
        return true;
    }
}
