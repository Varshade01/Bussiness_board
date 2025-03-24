package com.rdua.whiteboard.firebase.connection

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseConnectionImpl @Inject constructor(
) : FirebaseConnection {

    private var connected = false
    private val connectedRef = Firebase.database.getReference(".info/connected")

    private val connectionDeferred = CompletableDeferred<Boolean>()

    init {
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                connected = snapshot.getValue(Boolean::class.java) ?: false

                if (connected) {
                    connectionDeferred.complete(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                connected = false
                connectionDeferred.complete(false)
            }
        })
    }

    override fun getConnection() = connected

    override suspend fun waitConnection(): Boolean {
        if (connectionDeferred.isCompleted) {
            return connected
        }

        return try {
            withTimeout(3000) {
                connectionDeferred.await()
            }
        } catch (e: TimeoutCancellationException) {
            false
        }
    }
}
