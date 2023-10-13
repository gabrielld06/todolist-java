package com.supimpa.todolist.exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
        super("Entity not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
