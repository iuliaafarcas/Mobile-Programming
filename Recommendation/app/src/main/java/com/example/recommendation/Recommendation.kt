package com.example.recommendation

data class Recommendation(
    var title: String,
    var recommendedAge: Int,
    var benefits: String,
    var description: String,
    var type: TypeEnum,
    var id: Int = 0
)

