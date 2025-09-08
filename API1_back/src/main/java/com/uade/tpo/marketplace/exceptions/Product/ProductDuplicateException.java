package com.uade.tpo.marketplace.exceptions.Product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El Producto que se intenta agregar esta duplicada")
public class ProductDuplicateException extends Exception {

}