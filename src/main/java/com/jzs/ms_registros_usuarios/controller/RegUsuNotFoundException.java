package com.jzs.ms_registros_usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegUsuNotFoundException extends RuntimeException {
    
    public RegUsuNotFoundException(String message){
        super(message);
    }

}
