package com.example.springfileuploader.repository.models;

import lombok.Data;

@Data
public class FileDetail {
    private String id;
    private String originalFileName;
    private String contentType;

    public FileDetail(String id, String originalFileName, String contentType){
        this.id = id;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
    }
}
