package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.BlockingToken
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CheckBlockTokenValidityUseCaseImpl @Inject constructor() : CheckBlockTokenValidityUseCase {
    private val expirationInterval: Long = 30
    private val expirationTimeUnit: TimeUnit = TimeUnit.SECONDS
    override fun isTokenExpired(token: BlockingToken): Boolean {
        return token.isTokenExpired(expirationInterval, expirationTimeUnit)
    }
}