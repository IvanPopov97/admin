package ru.admin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ErrorResponse {
    private String param;
    private String message;
    private List<FieldError> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String param, String message) {
        this.param = param;
        this.message = message;
    }
}
