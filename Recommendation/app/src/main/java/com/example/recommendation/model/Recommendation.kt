package com.example.recommendation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation_table")
data class Recommendation(
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="recommendedAge")var recommendedAge: Int,
    @ColumnInfo(name="benefits")var benefits: String,
    @ColumnInfo(name="description")var description: String,
    @ColumnInfo(name="type")var type: TypeEnum,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") var id: Int?,
    @ColumnInfo(name="sentToServer")var sentToServer: Boolean=true


)

