package sharingcalender.calender.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sharingcalender.calender.dto.MessageDto;
import sharingcalender.calender.exception.AlreadyExistException;
import sharingcalender.calender.exception.AuthenticationException;
import sharingcalender.calender.exception.BadRequestException;
import sharingcalender.calender.exception.ResourceNotFoundException;
import sharingcalender.calender.exception.UnAuthorizedException;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<MessageDto> handleAlreadyExistException(AlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageDto> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<MessageDto> handleUnAuthorizedException(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(e.getMessage()));
    }
}
