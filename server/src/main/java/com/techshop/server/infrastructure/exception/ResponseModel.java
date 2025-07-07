package com.techshop.server.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {

    private HttpStatus status;

    private String message;

    private Instant timestamp = Instant.now();

    public ResponseModel(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
