package com.sparrow.Finance.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import com.sparrow.Finance.dto.ExceptionDTO;

@ControllerAdvice
public class FinanceExceptionHandler {
	
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ExceptionHandler(RecordNotFoundException.class)
	    public ExceptionDTO handleRecordtNotFoundException(RecordNotFoundException ex) {
	        ExceptionDTO exceptionDto = ExceptionDTO.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .message(ex.getMessage())
	                .build();
	        return exceptionDto;
	    }
	 
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ExceptionDTO handleIllegalArgumentException(IllegalArgumentException ex) {
	        ExceptionDTO exceptionDto = ExceptionDTO.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .message(ex.getMessage())
	                .build();
	        return exceptionDto;
	    }
	 
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ExceptionHandler(ResponseStatusException.class)
	    public ExceptionDTO handleResponseStatusException(ResponseStatusException ex) {
	        ExceptionDTO exceptionDto = ExceptionDTO.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .message(ex.getMessage())
	                .build();
	        return exceptionDto;
	    }
	 
	 
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ExceptionHandler(DiscountRetrievalException.class)
	    public ExceptionDTO handleDiscountRetrievalException(DiscountRetrievalException ex) {
	        ExceptionDTO exceptionDto = ExceptionDTO.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .message(ex.getMessage())
	                .build();
	        return exceptionDto;
	    }
	 
	 @ResponseStatus(HttpStatus.NOT_FOUND)
	    @ExceptionHandler(InternalServerException.class)
	    public ExceptionDTO handleInternalServerException(InternalServerException ex) {
	        ExceptionDTO exceptionDto = ExceptionDTO.builder()
	                .status(HttpStatus.NOT_FOUND)
	                .message(ex.getMessage())
	                .build();
	        return exceptionDto;
	    }

}
