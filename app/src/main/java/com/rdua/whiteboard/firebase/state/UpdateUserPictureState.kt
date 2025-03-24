package com.rdua.whiteboard.firebase.state

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface UpdateUserPictureState {
    class Success(val url: String = "") : UpdateUserPictureState
    object Failure : UpdateUserPictureState
}

/*
Using Contract API to smart cast UpdateUserPictureState after instance check
 */
@ExperimentalContracts
fun UpdateUserPictureState.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is UpdateUserPictureState.Success)
    }
    return this is UpdateUserPictureState.Success
}