package com.rdua.whiteboard.firebase.connection

interface FirebaseConnection {

    /**
     * Get Firebase Realtime database connection status
     *
     * @return Boolean connection value
     **/
    fun getConnection(): Boolean


    /**
     * Waiting for Firebase Realtime database connection status
     *
     * @return Boolean connection value
     **/
    suspend fun waitConnection(): Boolean
}