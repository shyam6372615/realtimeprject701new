package com.sparrow.Finance.dto;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response<T> {
    private int code;
    private String message;
    private T data;
  

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
       
    }
    public static class Data {
        private String REQUEST_PAYLOAD;
        private List<String> GLOBAL_FILE_UPLOAD;
        private String REQUEST_ID;
  
        
    }  
    

    // Getters and setters
}
