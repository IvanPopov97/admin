package ru.admin.dto;

import lombok.Data;
import ru.admin.enitity.UserRole;

@Data
public class UserResponseDto {
    private long id;
    private String email;
    private UserRole role;
}
