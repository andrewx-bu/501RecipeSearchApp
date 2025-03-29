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
}