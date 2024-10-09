package com.sparrow.Finance.Exception;

public class RecordNotFoundException extends RuntimeException {
	
	public RecordNotFoundException(String message) {
        super(message);
    }

	public RecordNotFoundException(String message,Exception e) {
        super(message,e);
    }

}
