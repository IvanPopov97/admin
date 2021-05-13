package ru.admin.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequestDto {
    @NotNull
    private Long id;

    @Email(message = "не похоже на реальный адрес электронной почты")
    @NotBlank(message = "не должно быть пустым")
    private String email;
}
