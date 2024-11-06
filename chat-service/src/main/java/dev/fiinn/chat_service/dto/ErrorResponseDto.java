package dev.fiinn.chat_service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ErrorResponseDto extends BaseResponseDto {
    private String error;
    private String path;
    private int status;
}
