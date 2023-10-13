package com.supimpa.todolist.exceptions;

public class InvalidAttributeValue extends Exception {
    public InvalidAttributeValue() {
        super("Invalid entity");
    }

    public InvalidAttributeValue(String message) {
        super(message);
    }
}