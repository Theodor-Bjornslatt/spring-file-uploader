package com.example.springfileuploader.controller.models;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserPasswordDTO {
    @NotNull
    @Email(regexp = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$",
    message = "Invalid Email")
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?\\-_&])[A-Za-z\\d@$!%*?\\-_&]{8,30}$",
            message = "Password must be 15 - 30 characters long and contain at least one uppercase letter, " +
                    "one lowercase letter, one number and one special character")
    private String password;
}
