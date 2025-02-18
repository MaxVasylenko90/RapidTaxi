package dev.mvasylenko.rapidtaxi.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserDto {
    @NotBlank
    @Length(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String name;

    @NotBlank
    @Length(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
