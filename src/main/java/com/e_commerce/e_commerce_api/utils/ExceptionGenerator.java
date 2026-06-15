package com.e_commerce.e_commerce_api.utils;

import com.e_commerce.e_commerce_api.exception.NotFoundException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionGenerator {
    public static NotFoundException handleNotFoundProduct(long id) {
        String message = String.format("Not found product with id = %d", id);
        return new NotFoundException(message);
    }

    public static NotFoundException handleNotFoundCart(long id) {
        String message = String.format("Not found cart with id = %d", id);
        return new NotFoundException(message);
    }

    public static NotFoundException handleNotFoundUser(long id) {
        String message = String.format("Not found user with id = %d", id);
        return new NotFoundException(message);
    }

    public static NotFoundException handleNotFoundColor(long id) {
        String message = String.format("Not found color with id = %d", id);
        return new NotFoundException(message);
    }

    public static NotFoundException handleNotFoundSize(long id) {
        String message = String.format("Not found size with id = %d", id);
        return new NotFoundException(message);
    }

    public static NotFoundException handleNotFoundAddress(long id) {
        String message = String.format("Not found address with id = %d", id);
        return new NotFoundException(message);
    }
}
