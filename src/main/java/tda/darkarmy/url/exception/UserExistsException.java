package tda.darkarmy.url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }
}
