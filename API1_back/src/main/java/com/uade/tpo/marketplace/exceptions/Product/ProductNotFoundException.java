package com.uade.tpo.marketplace.exceptions.Product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El Producto que se intenta buscar no existe")
public class ProductNotFoundException extends Exception{

}