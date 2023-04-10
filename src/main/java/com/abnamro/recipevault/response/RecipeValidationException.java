package com.abnamro.recipevault.response;

public class RecipeValidationException extends RuntimeException {
    private String errorMessage;

    public RecipeValidationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
