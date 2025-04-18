package br.com.zippydeliveryapi.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException (String entity, Long id) {
        super (String.format("No %s was found with id %s", entity, id));
    }
}