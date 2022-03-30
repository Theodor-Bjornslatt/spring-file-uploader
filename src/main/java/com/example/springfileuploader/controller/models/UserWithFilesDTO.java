package com.example.springfileuploader.controller.models;

import com.example.springfileuploader.repository.models.FileDetail;
import lombok.Getter;

import java.util.List;

public class UserWithFilesDTO {
    @Getter
    private final String id;
    @Getter
    private final String username;
    private final List<FileDetail> fileDetails;

    public UserWithFilesDTO(String id, String username, List<FileDetail> fileDetails){
        this.id = id;
        this.username = username;
        this.fileDetails = fileDetails;
    }

    public List<FileDetail> getFileDetails(){
        return List.copyOf(fileDetails);
    }

    @Override
    public String toString() {
        return "UserWithFilesDTO{" +
                "username='" + username + '\'' +
                ", fileDetails=" + fileDetails +
                '}';
    }
}
