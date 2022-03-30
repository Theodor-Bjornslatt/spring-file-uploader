package com.example.springfileuploader.repository.models;

import com.example.springfileuploader.controller.models.UserPasswordDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@Data
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<FileDetail> fileDetails = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

    public User(){

    }

    public User(String email){
        this.email = email;
    }

    public User(UserPasswordDTO newUser, List<String> roles){
        this.email = newUser.getEmail();
        this.password = newUser.getPassword();
        this.roles = roles;
    }

    public List<FileDetail> getFileDetails(){
        return List.copyOf(fileDetails);
    }

}
