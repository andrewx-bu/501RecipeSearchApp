package com.example.recipesearchapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface APIService {
    @GET("search.php")
    suspend fun getMealsBySearch(@Query("s") query: String): MealsResponse

    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") letter: String): MealsResponse

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
        // testing API failure
        // private const val BASE_URL = "https://www.themealdb.com/api/json/v1/2/"
        private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun create(): APIService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()
                .create(APIService::class.java)
        }
    }
}