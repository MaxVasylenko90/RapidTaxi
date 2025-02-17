package dev.mvasylenko.rapidtaxi.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UsetDto {
    @NotBlank
    @Length(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String name;

    @NotBlank
    @Length(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    private String password;

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
}
