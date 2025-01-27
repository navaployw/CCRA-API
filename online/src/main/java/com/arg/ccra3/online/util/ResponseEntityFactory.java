package com.arg.ccra3.online.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {

    private ResponseEntityFactory() {
    }
    
    public static ResponseEntity<Object> ok(Object obj){
        return ResponseEntity.ok().body(obj);
    }
    
    public static ResponseEntity<Object> notFound(Object obj){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(obj);
    }
    
    public static ResponseEntity<Object> internalServerError(Object obj){
        return ResponseEntity.internalServerError().body(obj);
    }
    
    public static ResponseEntity<Object> badRequest(Object obj){
        return ResponseEntity.badRequest().body(obj);
    }
}
