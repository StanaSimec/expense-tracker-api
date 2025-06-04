package com.simec.expense_tracker_api;

import com.simec.expense_tracker_api.dto.ErrorListResponseDto;
import com.simec.expense_tracker_api.dto.ErrorResponseDto;
import com.simec.expense_tracker_api.exception.ExpenseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleExpenseNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Expense not found"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorListResponseDto> handleMethodNotValid(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorListResponseDto(errors));
    }
}
