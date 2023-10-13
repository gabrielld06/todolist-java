package com.supimpa.todolist.exceptions;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException()  {
        super("Entity already exists");
    }

    public AlreadyExistsException(String message)  {
        super(message);
    }
}
