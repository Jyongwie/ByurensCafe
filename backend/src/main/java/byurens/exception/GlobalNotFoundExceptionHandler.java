package byurens.exception;

import java.time.LocalDateTime;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import byurens.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalNotFoundExceptionHandler {
    @ExceptionHandler(exception = OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(
        OptimisticLockingFailureException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "This record was just updated by another staff member. Please refresh the page and try again",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(exception = ByurensCafeException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
        ByurensCafeException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
        Exception ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occured on the server",
            LocalDateTime.now()
        );

        log.info("Critical Error: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
