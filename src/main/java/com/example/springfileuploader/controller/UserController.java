package com.example.springfileuploader.controller;

import com.example.springfileuploader.exception.InternalServerError;
import com.example.springfileuploader.exception.NoSuchEntryException;
import com.example.springfileuploader.service.UserService;
import com.example.springfileuploader.service.models.FileDetailWithResource;
import com.example.springfileuploader.repository.models.FileDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Saves a file belonging to the user who requested it.
     * @param file a {@link MultipartFile} containing the data of the file to be saved,
     *             sent with the request-parameter "file".
     * @param user the user authenticated by Spring Security.
     * @return a {@link FileDetail} containing information about the newly saved file.
     * @throws InternalServerError if something goes wrong and a user cannot be found or the
     * file could not be saved.
     */
    @PostMapping("/users/files/mine")
    public ResponseEntity<FileDetail> uploadFile(@RequestParam("file") MultipartFile file, Principal user)
            throws InternalServerError {
        try {
            FileDetail savedFileDetail = userService.saveFile(user.getName(), file).orElseThrow(() ->
                    new NoSuchEntryException("User not found")
            );
            log.info("Uploaded a file successfully");
            return ResponseEntity.status(HttpStatus.OK).body(savedFileDetail);
        } catch (IOException | NoSuchEntryException e){
            throw new InternalServerError();
        }
    }

    /**
     * Sends a response containing data of a file belonging to the user who requested it.
     * @param response the returned {@link HttpServletResponse}.
     * @param fileId the id of the file to download.
     * @param user the user authenticated by Spring Security.
     * @throws NoSuchEntryException if a file with the given id was not found.
     * @throws InternalServerError if reading of the file failed.
     */
    @GetMapping("/users/files/mine/{fileId}/download")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileId, Principal user)
            throws NoSuchEntryException, InternalServerError {
        FileDetailWithResource foundResourceDTO = userService.getFile(user.getName(), fileId).orElseThrow(() ->
                new NoSuchEntryException("No file with id=" + fileId)
        );
        response.setContentType(foundResourceDTO.getContentType());
        response.addHeader("Content-Disposition", "attachment; filename=" + foundResourceDTO.getTitle());
        try{
            FileCopyUtils.copy(foundResourceDTO.getFileResource().getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
            log.info("A user downloaded a file successfully");
        } catch(IOException e){
            throw new InternalServerError();
        }
    }

    /**
     * Deletes a file belonging to the user who requested it.
     * @param fileId the id of the file to be deleted.
     * @param user the user authenticated by Spring Security.
     * @return a {@link ResponseEntity} with a message of success.
     * @throws NoSuchEntryException if file cannot be deleted because it does not exist.
     */
    @DeleteMapping("/users/files/mine/{fileId}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String fileId, Principal user)
            throws NoSuchEntryException {
        boolean isDeleted = userService.deleteFile(user.getName(), fileId);
        if(!isDeleted){
            throw new NoSuchEntryException("No file with id=" + fileId + " for that user");
        }
        log.info("Deleted a file");
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Successfully deleted file with id=" + fileId
        ));
    }

    /**
     * Finds all fileDetails belonging to an authenticated user
     * @param user the user authenticated by Spring Security.
     * @return {@link List}<{@link FileDetail}> if fileDetails exist. Otherwise, an empty {@link List}.
     * @throws NoSuchEntryException if the user is not found.
     */
    @GetMapping("/users/files/mine")
    public List<FileDetail> getFiles(Principal user) throws NoSuchEntryException {
        return userService.getFileDetails(user.getName());
    }
}
