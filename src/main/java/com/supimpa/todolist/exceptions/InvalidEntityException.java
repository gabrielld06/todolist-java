package com.supimpa.todolist.exceptions;

public class InvalidEntityException extends Exception {
    public InvalidEntityException() {
        super("Invalid entity");
    }

    public InvalidEntityException(String message) {
        super(message);
    }
}
