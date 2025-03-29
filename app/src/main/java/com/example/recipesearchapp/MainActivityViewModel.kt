package com.example.recipesearchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    private val apiService = APIService.create()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _mealsList = MutableStateFlow<List<Meal>>(emptyList())
    val mealsList: StateFlow<List<Meal>> = _mealsList

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = apiService.getCategories()
                _categories.value = response.categories
                println("LOGGING: Fetched categories: ${response.categories}")
            } catch (e: Exception) {
                println("LOGGING: FAILED TO FETCH CATEGORIES - ${e.message}")
            }
        }
    }

    fun searchMeals(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getMealsBySearch(query)
                if (response.meals.isNullOrEmpty()) {
                    _mealsList.value = emptyList()
                    _errorMsg.value = "No meals found for \"$query\""
                } else {
                    _mealsList.value = response.meals
                    _errorMsg.value = null
                }
            } catch (e: Exception) {
                _mealsList.value = emptyList()
                _errorMsg.value = "Failed to fetch data by search: ${e.message}"
                println("LOGGING: FAILED TO FETCH DATA BY SEARCH")
            }
        }
    }

    fun searchMealsByFirstLetter(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getMealsByFirstLetter(query)
                if (response.meals.isNullOrEmpty()) {
                    _mealsList.value = emptyList()
                    _errorMsg.value = "No meals found for \"$query\""
                } else {
                    _mealsList.value = response.meals
                    _errorMsg.value = null
                }
            } catch (e: Exception) {
                _mealsList.value = emptyList()
                _errorMsg.value = "Failed to fetch data by search: ${e.message}"
                println("LOGGING: FAILED TO FETCH DATA BY SEARCHING FIRST LETTER")
            }
        }
    }
}