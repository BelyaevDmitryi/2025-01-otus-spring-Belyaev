package ru.otus.hw.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handeNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());

        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handeMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("", ex);

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        String errorMessage = allErrors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("." + System.lineSeparator()));

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handeInternalError(Exception ex) {
        log.error("", ex);

        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal error");
        return new ResponseEntity<ErrorDto>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
