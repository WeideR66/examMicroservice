package org.exam.examservice.responses.success;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class SuccessResponse {
    private int status;
    private String message;
}
