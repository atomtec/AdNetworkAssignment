package com.f11.testapp;

// Data operation result class
public class DataOperationResult {
    private boolean success;
    private String message; // Optional for error details

    public DataOperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return message;
    }
}
