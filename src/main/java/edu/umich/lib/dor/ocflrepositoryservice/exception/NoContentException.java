package edu.umich.lib.dor.ocflrepositoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NoContentException extends RuntimeException {
    public NoContentException(String message) {
        super(message);
    }
}