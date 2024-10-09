package com.sparrow.Finance.Exception;

public class DiscountRetrievalException extends RuntimeException {
	
	public DiscountRetrievalException(String message) {
        super(message);
    }

	public DiscountRetrievalException(String message,Exception e) {
        super(message,e);
    }

}
