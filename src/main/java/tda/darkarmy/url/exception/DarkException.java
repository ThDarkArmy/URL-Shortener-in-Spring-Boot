package tda.darkarmy.url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DarkException extends Exception {

    public DarkException(String message) {
        super(message);
    }
}
