package com.stock.stockify.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 예외를 전역(Global)으로 처리, 모든 컨트롤러에 대한 전역 에러 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 일반적인 런타임 예외 처리, RuntimeException 발생 시 이 메소드가 자동 호출 됨
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) { // 예외 발생 시
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400 Bad Request로 변경, HttpStatue.BAD_REQUEST: HTTP 상태 코드 400(클라이언트 잘못)으로 응답
                .body(e.getMessage()); // 에러 메시지를 응답에 담아서 전달 , e.getMessage(): 에러 메시지 그대로 반환
    }
    // @Valid 검증 실패 시 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage(); // 첫 번째 필드 에러 메시지 가져오기
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
    // 403 에러 메시지 반환
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbidden(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // 403 코드
                .body(e.getMessage()); // 예외 메시지 반환
    }
}
