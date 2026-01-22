package org.example.service;

/**
 * Classe per gestire gli errori
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
