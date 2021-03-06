package uk.me.thega.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends IOException {

    /** Generated ID. */
    private static final long serialVersionUID = -8774584389052301582L;

    public NotFoundException(final String reason) {
        super(reason);
    }

}
