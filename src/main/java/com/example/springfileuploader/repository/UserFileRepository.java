package com.example.springfileuploader.repository;

import com.example.springfileuploader.repository.models.FileDetail;
import com.example.springfileuploader.repository.models.User;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository {
    /**
     * Saves a {@link FileDetail} to a user in the database.
     * @param userId the {@link User} who owns the {@link FileDetail}.
     * @param fileDetailToSave the {@link FileDetail} to save.
     * @return {@code true} if save was successful, otherwise {@code false}.
     */
    boolean saveFileDetail(String userId, FileDetail fileDetailToSave);

    /**
     * Deletes a {@link FileDetail} from the given user in the database.
     * @param userId the id of the user who owns the file.
     * @param fileId the id of the file to delete.
     * @return {@code true} if deletion was successful, otherwise {@code false}.
     */
    boolean deleteFileDetail(String userId, String fileId);

    /**
     * Finds a {@link FileDetail} in the database, matching the given criteria .
     * @param userId The id of the user who owns the {@link FileDetail}.
     * @param fileId The id of the {@link FileDetail} to find.
     * @return An {@link Optional} of the {@link FileDetail} if
     * the {@code fileId} belongs to a user with the given {@code userId}. Otherwise {@code Optional.empty}.
     */
    Optional<FileDetail> findFileDetail(String userId, String fileId);

    /**
     * Finds all fileDetails belonging to {@link User} in the database.
     * @param userId the id of the user to get fileDetails from.
     * @return {@link List}<{@link FileDetail}> if user has any saved files. Otherwise, an empty {@link List}.
     */
    List<FileDetail> getAllFileDetails(String userId);
}

