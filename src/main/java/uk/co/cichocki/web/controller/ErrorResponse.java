package uk.co.cichocki.web.controller;

import lombok.Value;

@Value
class ErrorResponse {
    String message;
    String exception;
}
