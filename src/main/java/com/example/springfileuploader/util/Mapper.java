package com.example.springfileuploader.util;

import com.example.springfileuploader.service.models.FileDetailWithResource;
import org.springframework.core.io.FileSystemResource;

public class Mapper {
    private Mapper(){}

    /**
     * Creates a {@link FileDetailWithResource}.
     * @param resource the {@link FileSystemResource} to use.
     * @param fileName the original title of the file.
     * @param contentType the type of content the file contains.
     * @return a new {@link FileDetailWithResource} containing info from the given arguments.
     */
    public static FileDetailWithResource createFileDetailWithResource(FileSystemResource resource, String fileName, String contentType){
        return new FileDetailWithResource(resource, fileName, contentType);
    }
}
