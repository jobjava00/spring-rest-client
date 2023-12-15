package com.jobjava.spring.restclient

open class CustomException(
    override val message: String,
    throwable: Throwable? = null,
) : RuntimeException(message, throwable)
