package com.sparrow.Finance.Exception;

public class IllegalArgumentException extends RuntimeException {
	
	public IllegalArgumentException(String message) {
        super(message);
    }

	public IllegalArgumentException(String message,Exception e) {
        super(message,e);
    }
}
