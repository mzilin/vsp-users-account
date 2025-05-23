package com.mariuszilinskas.vsp.users.account.exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
        super("This email address is already associated with an account.");
    }

}
