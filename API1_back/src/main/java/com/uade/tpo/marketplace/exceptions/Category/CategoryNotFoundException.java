package com.uade.tpo.marketplace.exceptions.Category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La categoria que se intenta buscar no existe")
public class CategoryNotFoundException extends Exception {

}