package uk.me.thega.controller.exception;

import java.io.IOException;

/**
 * Exception for when the user does not have privilege.
 *
 * @author pwhittlesea
 */
public class AccessDeniedException extends IOException {

    /** Serial ID. */
    private static final long serialVersionUID = 4593945532937016478L;

    /**
     * Default constructor.
     *
     * @param reason the cause.
     */
    public AccessDeniedException(final String reason) {
        super(reason);
    }
}
