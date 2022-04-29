package com.openclassrooms.poseidon.service.exception;

public class UserAlreadyExistsException extends Exception{
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
