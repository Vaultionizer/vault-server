package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Config;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
public class FileService {
    public void writeToFile(String content, Long spaceID, Long saveIndex){
        File f = new File(getFilePath(spaceID, saveIndex));
        if (!f.exists()){
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(f, false))){
            writer.print(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
