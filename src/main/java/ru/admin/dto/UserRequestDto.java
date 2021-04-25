package ru.admin.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private Long id;
    private String email;
    private String password;

    public UserRequestDto withoutId() {
        this.id = null;
        return this;
    }

    public UserRequestDto withPassword(String password) {
        this.password = password;
        return this;
    }
}
