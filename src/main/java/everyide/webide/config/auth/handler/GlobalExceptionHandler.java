package everyide.webide.config.auth.handler;

import everyide.webide.config.auth.exception.EmailAlreadyUsedException;
import everyide.webide.config.auth.exception.NoRoomException;
import everyide.webide.config.auth.exception.RoomDestroyException;
import everyide.webide.config.auth.exception.ValidateRoomException;
import everyide.webide.room.domain.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<Object> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomDestroyException.class)
    public ResponseEntity<Object> handleRoomDestroyException(RoomDestroyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidateRoomException.class)
    public ResponseEntity<Object> handleRoomDestroyException(ValidateRoomException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoRoomException.class)
    public ResponseEntity<Object> handleRoomDestroyException(NoRoomException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }
}