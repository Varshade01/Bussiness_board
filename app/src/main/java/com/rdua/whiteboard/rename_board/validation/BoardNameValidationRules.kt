package com.rdua.whiteboard.rename_board.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardNameValidationRules @Inject constructor() {
    val minLength = 1
    val maxLength = 100
}