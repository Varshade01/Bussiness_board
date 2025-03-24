package com.rdua.whiteboard.common.usecases

interface GetDeviceIdUseCase {
    suspend operator fun invoke(): String
}