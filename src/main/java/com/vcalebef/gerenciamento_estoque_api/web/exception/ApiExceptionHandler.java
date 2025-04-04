package com.vcalebef.gerenciamento_estoque_api.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vcalebef.gerenciamento_estoque_api.enums.TipoBebida;
import com.vcalebef.gerenciamento_estoque_api.exception.EntityNotFoundException;
import com.vcalebef.gerenciamento_estoque_api.exception.StockCapacityException;
import com.vcalebef.gerenciamento_estoque_api.exception.TypeDrinkInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
                                                                HttpServletRequest request) {
        log.error("Api error - ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(TypeDrinkInvalidException.class)
    public ResponseEntity<ErrorMessage> typeDrinkInvalidException(RuntimeException ex,
                                                                HttpServletRequest request) {
        log.error("Api error - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getCause().getMessage()));

    }

    @ExceptionHandler(StockCapacityException.class)
    public ResponseEntity<ErrorMessage> stockCapacityException(RuntimeException ex,
                                                                  HttpServletRequest request) {
        log.error("Api error - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request,
                                                                        BindingResult result) {
        log.error("Api error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalidos", result));

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                     HttpServletRequest request) {

        String mensagem = "Erro ao ler os dados enviados. Verifique os valores informados no corpo da requisição.";

        if (ex.getCause() instanceof InvalidFormatException formatException &&
                formatException.getTargetType().isEnum()) {

            Class<?> enumClass = formatException.getTargetType();

            if (enumClass.equals(TipoBebida.class)) {
                mensagem = "Valor inválido para o tipo de bebida. Os valores aceitos são: " +
                        Arrays.stream(TipoBebida.values())
                                .map(Enum::name)
                                .collect(Collectors.joining(", "));
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, mensagem));
    }

}
