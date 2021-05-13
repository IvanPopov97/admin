package ru.admin.enitity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("confirmation_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ConfirmationToken {
    @Id
    private Long id;
    private String code;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private Long userId;
}
