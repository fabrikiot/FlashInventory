package com.flash.inventory.extras

import com.flash.inventory.data.network.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    public const val BASE_URL = "https://inventory-qr.intellicar.io/"

    val instance: ApiInterface2 by lazy {

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(ApiInterface2::class.java)

    }
}