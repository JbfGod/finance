package org.finance.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.finance.infrastructure.common.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleHxException(Exception e) {
        e.printStackTrace();
        return R.error(e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handleValidationException(Exception e) {
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException except = (ConstraintViolationException) e;
            for (ConstraintViolation<?> error : except.getConstraintViolations()) {
                return R.warn(error.getMessage());
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException except = (MethodArgumentNotValidException) e;
            for (ObjectError error : except.getBindingResult().getAllErrors()) {
                return R.warn(error.getDefaultMessage());
            }
        }
        return R.warn("请求参数不合法！");
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R handleAccessDeniedException(AccessDeniedException e) {
        return R.warn("权限不足！");
    }

}
