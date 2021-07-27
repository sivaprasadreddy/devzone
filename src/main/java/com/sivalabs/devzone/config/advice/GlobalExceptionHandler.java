package com.sivalabs.devzone.config.advice;

import com.sivalabs.devzone.api.controllers.LinkRestController;
import com.sivalabs.devzone.domain.exceptions.BadRequestException;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@Slf4j
@RestControllerAdvice(basePackageClasses = LinkRestController.class)
public class GlobalExceptionHandler implements ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return this.create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler({
        DevZoneException.class,
        BadRequestException.class,
        ResourceAlreadyExistsException.class
    })
    public ResponseEntity<Problem> handleDevZoneException(
            DevZoneException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return this.create(Status.BAD_REQUEST, exception, request);
    }
}
