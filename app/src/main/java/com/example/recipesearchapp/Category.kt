package com.example.recipesearchapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesResponse(
    @Json(name = "categories") val categories: List<Category>
)

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "idCategory") val id: String,
    @Json(name = "strCategory") val name: String,
    @Json(name = "strCategoryThumb") val thumbnailUrl: String,
    @Json(name = "strCategoryDescription") val description: String
)
