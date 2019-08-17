package com.example.paulinamonroy.liverpool.services

import com.example.paulinamonroy.liverpool.MainActivity
import com.example.paulinamonroy.liverpool.interfaces.ApiService
import com.example.paulinamonroy.liverpool.model.Product
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl("https://shoppapp.liverpool.com.mx/appclienteservices/services/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create<ApiService>(ApiService::class.java)


    fun getProducts(search:String) : List<Product>{
        var post : JsonObject?
        var listProducts : MutableList<Product> = mutableListOf<Product>()

        service.getProduct(search,1,50).enqueue(object : Callback<JsonObject>{

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?){
                post = response?.body()
                val results: JsonObject? = post?.getAsJsonObject("plpResults")
                val eleArray : JsonArray? = results?.getAsJsonArray("records")

                for(prod in eleArray!!){
                    listProducts.add(Gson().fromJson(prod, Product::class.java))
                }

                MainActivity.setRecyclerView();

            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })

        return listProducts

    }
}