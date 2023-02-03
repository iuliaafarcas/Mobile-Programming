package com.example.recommendation.data


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.recommendation.DAO.RecommendationRoomDatabase
import com.example.recommendation.RecommendationRepository
import com.example.recommendation.WebSocket.WebSocket
import com.example.recommendation.model.Recommendation
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class Datasource(application: Application) : AndroidViewModel(application) {
    private val repository: RecommendationRepository
    private val webSocket: WebSocket
    private val _recommendationList: LiveData<MutableList<Recommendation>>
    val recommendationList get() = _recommendationList
    val connected: LiveData<Boolean>
    companion object{
        val TAG="Datasource"
    }

    init {
        val recommendationDao =
            RecommendationRoomDatabase.getDatabase(application).recommendationDAO()
        repository = RecommendationRepository(recommendationDao)
        _recommendationList = repository.allRecommendations.asLiveData()

        val sessionHandler = WebSocket.StompSessionHandler(application,viewModelScope)
        webSocket = WebSocket.getConnection(sessionHandler){
            syncData()
        }
        connected= webSocket.isConnectedObserver
        thread {
            webSocket.connect()
        }

    }

    fun getData(id: Int): Recommendation? {
        return recommendationList.value?.find { it.id == id }
    }

    fun removeData(id: Int) = thread {
        if(webSocket.isConnected()==true){
            Log.d(TAG,"deleting element $id from server")
            webSocket.sendDeleteReq(id)
        }
    }

    fun addData(recommendation: Recommendation) = thread{
        if(webSocket.isConnected()==true){
            Log.d(TAG,"adding element $recommendation on server")
            webSocket.sendWriteReq(recommendation)
        }
        else {
            Log.d(TAG,"adding element $recommendation on database")
            recommendation.sentToServer=false
            viewModelScope.launch { repository.insert(recommendation) }
        }
    }

    fun updateData(recommendation: Recommendation) = thread{
        if(webSocket.isConnected()==true){
            Log.d(TAG,"updating element $recommendation on server")
            webSocket.sendUpdateReq(recommendation)
        }

    }
    private fun syncData(){
        thread{
            val recommendationsToSend: List<Recommendation> = recommendationList.value!!.filter { !it.sentToServer }
            viewModelScope.launch {
                recommendationsToSend.forEach {
                    repository.delete(it.id!!)
                    it.id = null
                    addData(it)
                }
            }

        }
    }

}