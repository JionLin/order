package com.example.orderdemo.order.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps domain and Feign-propagated errors to clear 4xx/5xx responses.
 *
 * @author jiaolin
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle missing order.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 404 body
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handle missing SKU from inventory.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 404 body
     */
    @ExceptionHandler(SkuNotFoundException.class)
    public ResponseEntity<ApiError> handleSkuNotFound(SkuNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handle oversell / insufficient stock — expected demo 4xx.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 409 Conflict
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleInsufficient(InsufficientStockException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handle validation failures.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 400 body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Validation failed");
        return build(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handle unexpected inventory remote failures.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 502 body
     */
    @ExceptionHandler(InventoryRemoteException.class)
    public ResponseEntity<ApiError> handleRemote(InventoryRemoteException ex, HttpServletRequest request) {
        LOGGER.error("Inventory remote failure on {}", request.getRequestURI(), ex);
        return build(HttpStatus.BAD_GATEWAY, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Fallback handler.
     *
     * @param ex exception
     * @param request HTTP request
     * @return 500 body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        LOGGER.error("Unhandled order error on {}", request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String path) {
        ApiError body = new ApiError(status.value(), status.getReasonPhrase(), message, path);
        return ResponseEntity.status(status).body(body);
    }
}
