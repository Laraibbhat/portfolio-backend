package com.portfolio.backend.exception;

public class UserNotFoundException extends RuntimeException {
   public UserNotFoundException(String username) {
	  super("Profile not found for username: " + username);
   }
}