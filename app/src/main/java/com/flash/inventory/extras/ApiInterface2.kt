package com.flash.inventory.extras

import com.flash.inventory.data.models.UploadImageModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface2 {

    @Multipart
    @POST("api/image/v1/upload")
    fun imageUpload(

        @Part("qrcode") qrcode: RequestBody,
        @Part images: List<MultipartBody.Part>

    ): Call<UploadImageModel>

}