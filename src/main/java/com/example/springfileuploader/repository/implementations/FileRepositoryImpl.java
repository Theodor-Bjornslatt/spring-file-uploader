package com.example.springfileuploader.repository.implementations;

import com.example.springfileuploader.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileRepositoryImpl implements FileRepository {
    @Value("${default_filepath}")
    private String DEFAULT_PATH;

    @Override
    public void saveFileToSystem(String userId, MultipartFile fileToSave, String fileId) throws IOException {
        File userDirectory = new File(DEFAULT_PATH, userId);
        userDirectory.mkdirs();
        Path path = Path.of(DEFAULT_PATH, userId, fileId);
        Files.copy(fileToSave.getInputStream(), path);
    }

    @Override
    public FileSystemResource findInFileSystem(String userId, String fileId){
        return new FileSystemResource(Path.of(DEFAULT_PATH, userId, fileId));
    }

    @Override
    public boolean deleteFromSystem(String userId, String fileId) {
        try {
            Files.deleteIfExists(Path.of(DEFAULT_PATH, userId, fileId));
            Path userDirectoryPath = Path.of(DEFAULT_PATH, userId);
            DirectoryStream<Path> stream = Files.newDirectoryStream(userDirectoryPath);
            if(!stream.iterator().hasNext()) {
                Files.delete(userDirectoryPath);
            }
            return true;
        } catch(IOException e){
            return false;
        }
    }
}
