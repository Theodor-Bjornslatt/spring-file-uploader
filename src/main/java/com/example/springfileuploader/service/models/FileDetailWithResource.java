package com.example.springfileuploader.service.models;

import lombok.Getter;
import org.springframework.core.io.FileSystemResource;

@Getter
public class FileDetailWithResource {
    private final FileSystemResource fileResource;
    private final String title;
    private final String contentType;

    public FileDetailWithResource(FileSystemResource fileResource, String title, String contentType){
        this.fileResource = fileResource;
        this.title = title;
        this.contentType = contentType;
    }
}
