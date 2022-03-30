package com.example.springfileuploader.repository;

import com.example.springfileuploader.repository.models.User;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public interface FileRepository {
    /**
     * Creates a new directory for the user who owns the file, if it doesn't already exist,
     * then saves the received {@link MultipartFile} to the user-directory using the id of the file as a name.
     * @param userId The id of the {@link User} who owns the file.
     * @param fileToSave The {@link MultipartFile} to be saved.
     * @param fileId the generated id of the file.
     * @throws IOException if writing the file to the system fails.
     */
    void saveFileToSystem(String userId, MultipartFile fileToSave, String fileId) throws IOException;

    /**
     * @return A {@link FileSystemResource} containing the path to the file.
     */
    FileSystemResource findInFileSystem(String userId, String fileId);

    /**
     * Deletes a file from a directory belonging to a {@link User}.
     * If the associated user-directory is emptied in the process, it is also deleted.
     * @param userId the id of the {@link User} who owns the file.
     * @param fileId the id used as the name of the file to delete.
     * @return {@code true} if deletion is successful, otherwise {@code false}.
     */
    boolean deleteFromSystem(String userId, String fileId);
}
