package br.com.zippydeliveryapi.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class PromoCodeException extends RuntimeException {

    public static final String MESSAGE_INVALID_DATE = "Invalid validity date. Please check if the end date is after the start date.";

    public PromoCodeException (String message) {
        super (String.format(message));
    }

}