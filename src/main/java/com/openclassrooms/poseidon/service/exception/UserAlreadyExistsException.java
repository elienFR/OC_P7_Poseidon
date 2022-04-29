package com.openclassrooms.poseidon.service.exception;

/**
 *
 * This exception is thrown when you try to add a new user who have the same username as an existing user.
 *
 */
public class UserAlreadyExistsException extends Exception{
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
