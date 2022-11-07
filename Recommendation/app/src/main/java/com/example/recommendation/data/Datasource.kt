package com.example.recommendation.data


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recommendation.Recommendation
import com.example.recommendation.TypeEnum

class Datasource : ViewModel() {
    private val _recommendationList: MutableLiveData<MutableList<Recommendation>> = MutableLiveData(
        mutableListOf()
    )
    val recommendationList get() = _recommendationList
    private var currentId = 1

    init {
        loadRecommendations()
    }

    private fun loadRecommendations() {

        addItemToList(Recommendation("Tennis", 6, "dada", "descr1", TypeEnum.Sport))
        addItemToList(Recommendation("Dog man", 6, "b2", "descr2", TypeEnum.Book))
        addItemToList(Recommendation("Lilo and Stitch", 5, "b3", "descr3", TypeEnum.Other ))
        addItemToList(Recommendation("Zucchini fritters", 8, "b4", "descr4", TypeEnum.FoodRecipe))
        addItemToList(Recommendation("The Magic Faraway Tree", 7, "b5", "descr5", TypeEnum.Book))
        addItemToList(Recommendation("Gymnastics", 1, "b6", "descr6", TypeEnum.Sport))


    }

    fun getList(): MutableList<Recommendation> {
        return recommendationList.value!!
    }

    fun addItemToList(item: Recommendation) {
        val list = recommendationList.value
        item.id = currentId
        currentId++
        list!!.add(item)
        recommendationList.value = list!!
    }
    fun updateItem(item:Recommendation){
        val list = recommendationList.value
        list?.find { it.id == item.id }?.also {
            it.benefits = item.benefits
            it.type = item.type
            it.title = item.title
            it.recommendedAge = item.recommendedAge
            it.description = item.description
        }
        recommendationList.value = list
    }

    fun findById(id: Int): Recommendation? {
        return recommendationList.value!!.find { it.id == id }
    }



}