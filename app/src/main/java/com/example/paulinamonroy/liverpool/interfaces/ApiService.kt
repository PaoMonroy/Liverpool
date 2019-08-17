package com.example.paulinamonroy.liverpool.interfaces

import com.google.gson.JsonObject
import retrofit2.*
import retrofit2.http.*
import retrofit2.http.GET



interface ApiService {

    @GET("plp?force-plp=true")
    fun getProduct(@Query("search-string") searchString: String,
                @Query("page-number") pageNum: Int,
                @Query("number-of-items-per-page") numItem: Int
                ): Call<JsonObject>

}