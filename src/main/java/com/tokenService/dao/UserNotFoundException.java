package com.tokenService.dao;

class UserNotFoundException extends RuntimeException {

	UserNotFoundException(Long id) {
		super("Could not find user " + id);
	}
}
