package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.BlockingToken

interface CheckBlockTokenValidityUseCase {
    fun isTokenExpired(token: BlockingToken): Boolean
}