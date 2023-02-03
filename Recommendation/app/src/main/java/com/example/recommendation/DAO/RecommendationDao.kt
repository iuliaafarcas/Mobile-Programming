package com.example.recommendation.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.recommendation.model.Recommendation
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    @Query("SELECT * FROM recommendation_table")
    fun getRecommendations(): Flow<MutableList<Recommendation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recommendation: Recommendation)

    @Query("DELETE FROM recommendation_table WHERE id= :recommendationId")
    suspend fun delete(recommendationId: Int)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(recommendation: Recommendation)

}