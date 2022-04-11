package org.finance.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.finance.infrastructure.common.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author jiangbangfa
 */
@Slf4j
@RestControllerAdvice
@ApiIgnore
public class ExceptionAdviceWeb {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleHxException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(R.error(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleValidationException(ConstraintViolationException e) {
        for (ConstraintViolation<?> s : e.getConstraintViolations()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(R.warn(s.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.warn("请求参数不合法！"));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(R.warn("权限不足！"));
    }

}
