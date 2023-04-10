package com.abnamro.recipevault.response;

public record ErrorResponse(Integer httpStatus, String exception, String message) {
}