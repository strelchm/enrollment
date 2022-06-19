package ru.strelchm.enrollment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.strelchm.enrollment.api.dto.Error;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

@EnableWebMvc
@RestControllerAdvice
public class GlobalExceptionHandler {
  private Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());

  @ExceptionHandler({BadRequestException.class, MethodArgumentTypeMismatchException.class,
      ConstraintViolationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handleBadRequestExceptions(Exception ex) {
    ex.printStackTrace();
    return getResponseFromException(400, "Невалидная схема документа или входные данные не верны");
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public Error handleNotFoundExceptions(Exception ex) {
    ex.printStackTrace();
    return getResponseFromException(404, "Категория/товар не найден");
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public Error handleIntervalServerErrorExceptions(Exception ex) {
    LOG.severe(String.format("Internal error: %s", ex.getMessage()));
    ex.printStackTrace();
    return getResponseFromException(500, "Внутренняя ошибка");
  }

  @NotNull
  private Error getResponseFromException(int exCode, String exMessage) {
    return new Error().code(exCode).message(exMessage);
  }
}
