package com.example.springfileuploader.service;

import com.example.springfileuploader.repository.models.User;
import com.example.springfileuploader.service.models.FileDetailWithResource;
import com.example.springfileuploader.repository.models.FileDetail;
import com.example.springfileuploader.exception.NoSuchEntryException;
import com.example.springfileuploader.repository.FileRepository;
import com.example.springfileuploader.repository.UserRepository;
import com.example.springfileuploader.util.Mapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Autowired
    public UserService(UserRepository repository,  FileRepository fileRepository){
        this.userRepository = repository;
        this.fileRepository = fileRepository;
    }

    /**
     * Looks up a user by email
     * @param email the email of the {@link User} to search for.
     * @return the entire {@link User} associated with the given email.
     */
    public User findByEmail(String email) throws NoSuchEntryException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NoSuchEntryException("No user found with email=" + email)
        );
    }

    /**
     * Saves a file to a user collection and a filesystem.
     * @param email The email of the existing user who owns the file.
     * @param multipartFile The {@link MultipartFile} to save.
     * @return An {@link Optional} containing the saved {@link FileDetail},
     * if successfully saved to user collection. Otherwise {@code Optional.empty}.
     * @throws IOException if file could not be saved to system.
    */
    public Optional<FileDetail> saveFile(String email, MultipartFile multipartFile) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) return Optional.empty();

        String userId = optionalUser.get().getId();
        String fileId = String.valueOf(new ObjectId());
        fileRepository.saveFileToSystem(userId, multipartFile, fileId);
        FileDetail fileDetailToSave = new FileDetail(
                fileId,
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType()
        );
        boolean isFileSaved = userRepository.saveFileDetail(userId, fileDetailToSave);
        if (!isFileSaved) {
            fileRepository.deleteFromSystem(userId, fileId);
            return Optional.empty();
        }
        return Optional.of(fileDetailToSave);
    }

    /**
     * Finds a {@link FileSystemResource} and its corresponding information.
     * @param email the email belonging to the user who owns the wanted file.
     * @param fileId the id of the file to find.
     * @return an {@link Optional} of a {@link FileDetailWithResource} containing the {@link FileSystemResource}
     * and {@link FileDetail} pertaining to the requested file. {@code Optional.empty} if the needed {@link FileDetail}
     * could not be found.
     */
    public Optional<FileDetailWithResource> getFile(String email, String fileId) throws NoSuchEntryException {
        User user =  this.findByEmail(email);
        Optional<FileDetail> existingFileDetail = userRepository.findFileDetail(user.getId(), fileId);

        if(existingFileDetail.isEmpty()) return Optional.empty();

        FileSystemResource fileResource = fileRepository.findInFileSystem(user.getId(), fileId);
        return Optional.of(Mapper.createFileDetailWithResource(
                fileResource,
                existingFileDetail.get().getOriginalFileName(),
                existingFileDetail.get().getContentType()
        ));
    }

    /**
     * Deletes a file and all its related data.
     * @param email the email of the user who owns the file to delete.
     * @param fileId the id of the file to delete.
     * @return {@code true} if deletion of all data pertaining to the file was successful, otherwise {@code false}.
     * @throws NoSuchEntryException if user cannot be found or the {@link FileDetail} could not be deleted.
     */
    public boolean deleteFile(String email, String fileId) throws NoSuchEntryException {
        User user = this.findByEmail(email);
        boolean isFileDetailDeleted = userRepository.deleteFileDetail(user.getId(), fileId);
        if(!isFileDetailDeleted){
            throw new NoSuchEntryException("No fileDetail with id=" + fileId + " for that user");
        }
        return fileRepository.deleteFromSystem(user.getId(), fileId);
    }

    /**
     * Looks up an authenticated user and finds the fileDetails belonging to them.
     * @param email the email of the user who owns the wanted files.
     * @return a {@link List}<{@link FileDetail}> if fileDetails are found, otherwise an empty {@link List}.
     * @throws NoSuchEntryException if user cannot be found.
     */
    public List<FileDetail> getFileDetails(String email) throws NoSuchEntryException {
        User user = this.findByEmail(email);
        return userRepository.getAllFileDetails(user.getId());
    }


}
