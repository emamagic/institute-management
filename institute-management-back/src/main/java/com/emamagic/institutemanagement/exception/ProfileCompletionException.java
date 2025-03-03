package com.emamagic.institutemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_REQUIRED)
public class ProfileCompletionException extends RuntimeException {
  public ProfileCompletionException(String message) {
    super(message);
  }
}
