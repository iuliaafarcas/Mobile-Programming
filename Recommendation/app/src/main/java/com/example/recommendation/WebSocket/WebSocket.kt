package com.example.recommendation.WebSocket

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.recommendation.DAO.RecommendationRoomDatabase
import com.example.recommendation.RecommendationRepository
import com.example.recommendation.model.Recommendation
import com.example.recommendation.model.TypeEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import kotlin.collections.LinkedHashMap

class WebSocket(
    private val sessionHandler: StompSessionHandler,
    private val syncFunction: () -> Unit

) {
    private val webSocketTransport = WebSocketTransport(StandardWebSocketClient())
    private val sockJsClient = SockJsClient(listOf(webSocketTransport))
    private val stompClient = WebSocketStompClient(sockJsClient).apply {
        messageConverter = MappingJackson2MessageConverter();
    }
    private var stompSession: StompSession? = null
    val isConnectedObserver: MutableLiveData<Boolean> = MutableLiveData(false)

    companion object {
        const val TAG = "WebSocket"
        const val URL = "http://172.30.115.26:8080"
        const val CONNECTION_URL = "/hello"
        const val WRITE_URL = "/recommendation/create"
        const val RECEIVE_WRITE_URL = "/entities/create"
        const val READ_URL = "/read"
        const val UPDATE_URL = "/recommendation/update"
        const val RECEIVE_UPDATE_URL = "/entities/update"
        const val DELETE_URL = "/recommendation/delete"
        const val RECEIVE_DELETE_URL = "/entities/delete"


        @Volatile
        private var INSTANCE: WebSocket? = null

        fun getConnection(
            sessionHandler: StompSessionHandler,
            syncFunction: () -> Unit
        ): WebSocket {
            return INSTANCE ?: synchronized(this) {
                val instance = WebSocket(sessionHandler, syncFunction)
                INSTANCE = instance
                instance
            }
        }
    }

    @WorkerThread
    fun sendWriteReq(recommendation: Recommendation) {
        try {
            Log.d(TAG, "Sending write request $recommendation")
            stompSession?.send(WRITE_URL, recommendation)
        } catch (e: Exception) {
            Log.e(TAG, "error", e)
            this.isConnectedObserver.postValue(false)
        }
    }

    @WorkerThread
    fun sendUpdateReq(recommendation: Recommendation) {
        try {
            Log.d(TAG, "Sending update request $recommendation")
            stompSession?.send(UPDATE_URL, recommendation)
        } catch (e: Exception) {
            Log.e(TAG, "error", e)
            this.isConnectedObserver.postValue(false)
        }
    }

    @WorkerThread
    fun sendDeleteReq(id: Int) {
        try {
            Log.d(TAG, "Sending delete request $id")
            stompSession?.send(DELETE_URL, id)
        } catch (e: Exception) {
            Log.e(TAG, "error", e)
            this.isConnectedObserver.postValue(false)
        }
    }

    @WorkerThread
    fun sendReadRequest(): List<*>? {
        val client: WebClient = WebClient.create(URL)
        val response = client.get().uri(READ_URL).retrieve().bodyToMono(List::class.java)
        return response.block()
    }

    @WorkerThread
    fun connect() {
        if (stompSession == null) {
            Log.d(TAG, "Connecting to server")
            try {
                stompSession = stompClient.connect(URL + CONNECTION_URL, sessionHandler).get()
                val isConnected = stompSession?.isConnected
                Log.d(TAG, if (isConnected == true) "Connected" else "Not connected")
                isConnectedObserver.postValue(true)
                syncFunction()
            } catch (e: Exception) {
                Log.e(TAG, "error", e)

            }
        }
    }


    @WorkerThread
    fun disconnect() {
        if (stompSession != null && stompSession!!.isConnected) {
            Log.d(TAG, "Disconnecting")
            stompSession!!.disconnect()
            stompSession = null
            isConnectedObserver.postValue(false)
        }

    }

    fun isConnected(): Boolean? {
        return stompSession?.isConnected
    }

    class StompSessionHandler(
        application: Application,
        private val scope: CoroutineScope
    ) : StompSessionHandlerAdapter() {
        private val repository: RecommendationRepository

        init {
            val recommendationDao =
                RecommendationRoomDatabase.getDatabase(application).recommendationDAO()
            repository = RecommendationRepository(recommendationDao)
        }

        fun convertRecommendation(map: LinkedHashMap<String, Any>): Recommendation {
            return Recommendation(
                map["title"] as String,
                map["recommendedAge"] as Int,
                map["benefits"] as String,
                map["description"] as String,
                TypeEnum.valueOf(map["type"] as String),
                map["id"] as Int?

            )
        }

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
            session.subscribe(RECEIVE_WRITE_URL, object : StompFrameHandler {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    val convertedPayload = payload as LinkedHashMap<String, Any>
                    scope.launch {
                        Log.d(TAG, "Saving received entity ${convertedPayload}")
                        repository.insert(convertRecommendation(convertedPayload))
                    }
                }
                override fun getPayloadType(headers: StompHeaders): Type {
                    return LinkedHashMap::class.java
                }
            })

            session.subscribe(RECEIVE_UPDATE_URL, object : StompFrameHandler {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    val convertedPayload = payload as LinkedHashMap<String, Any>
                    scope.launch {
                        Log.d(TAG, "Updating received entity $convertedPayload")
                        repository.update(convertRecommendation(convertedPayload))
                    }
                }
                override fun getPayloadType(headers: StompHeaders): Type {
                    return LinkedHashMap::class.java
                }
            })

            session.subscribe(RECEIVE_DELETE_URL, object : StompFrameHandler {
                override fun handleFrame(headers: StompHeaders, payload: Any?) {
                    val convertedId = payload as Int
                    scope.launch {
                        Log.d(TAG, "Deleting received entity $convertedId")
                        repository.delete(convertedId)
                    }
                }
                override fun getPayloadType(headers: StompHeaders): Type {
                    return Int::class.java
                }
            })

        }

    }


}