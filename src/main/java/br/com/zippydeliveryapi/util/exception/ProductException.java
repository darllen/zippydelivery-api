package br.com.zippydeliveryapi.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductException extends RuntimeException {

    public static final String MESSAGE_PRODUCT_UNAVAILABLE = "It is not allowed to add unavailable products.";

    public ProductException (String message) {
        super (String.format(message));
    }
}