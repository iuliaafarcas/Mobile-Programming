package com.example.recommendation

import androidx.annotation.WorkerThread
import com.example.recommendation.DAO.RecommendationDao
import com.example.recommendation.model.Recommendation
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class RecommendationRepository(private val recommendationDao: RecommendationDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allRecommendations: Flow<MutableList<Recommendation>> = recommendationDao.getRecommendations()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(recommendation: Recommendation) {
        recommendationDao.insert(recommendation )
    }

    @WorkerThread
    suspend fun delete(id: Int) {
        recommendationDao.delete(id )
    }

    @WorkerThread
    suspend fun update(recommendation: Recommendation) {
        recommendationDao.update(recommendation )
    }

}