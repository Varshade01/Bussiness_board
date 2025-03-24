package com.rdua.whiteboard.exceptions

class InvalidValueModelException(
    message: String = "Invalid value for this item",
) : IllegalArgumentException(message)
