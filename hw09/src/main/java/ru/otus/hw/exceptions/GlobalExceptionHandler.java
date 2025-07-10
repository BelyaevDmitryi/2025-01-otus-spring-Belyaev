package ru.otus.hw.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handeNotFoundException(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error", HttpStatus.NOT_FOUND);
        modelAndView.addObject("errorText", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handeInternalError(Exception ex) {
        System.out.println("ERROR: Exception is " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView("error", HttpStatus.INTERNAL_SERVER_ERROR);
        modelAndView.addObject("errorText",
                "The app couldn't handle your magical touches.");

        return modelAndView;
    }

}
