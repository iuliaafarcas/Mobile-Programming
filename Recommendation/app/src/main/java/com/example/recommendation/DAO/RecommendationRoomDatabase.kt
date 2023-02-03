package com.example.recommendation.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recommendation.model.Recommendation


@Database(entities = arrayOf(Recommendation::class), version = 2, exportSchema = false)
public abstract class RecommendationRoomDatabase : RoomDatabase() {

    abstract fun recommendationDAO(): RecommendationDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RecommendationRoomDatabase? = null

        fun getDatabase(context: Context): RecommendationRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendationRoomDatabase::class.java,
                    "recommendation_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}