package com.example.recipesearchapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealsResponse(
    @Json(name = "meals") val meals: List<Meal>?
)

@JsonClass(generateAdapter = true)
data class Meal(
    @Json(name = "strMeal") val name: String,
    @Json(name = "strCategory") val category: String,
    @Json(name = "strArea") val area: String,
    @Json(name = "strInstructions") val instructions: String,
    @Json(name = "strMealThumb") val picURL: String,
)