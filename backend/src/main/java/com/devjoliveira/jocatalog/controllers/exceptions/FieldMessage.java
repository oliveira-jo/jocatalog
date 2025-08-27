package com.devjoliveira.jocatalog.controllers.exceptions;

public record FieldMessage(
    String fieldName, String message) {
}
