package com.flash.inventory.data.network

import com.flash.inventory.data.models.CheckDeviceModel
import com.flash.inventory.data.models.UploadImageModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/image/v1/fetchqr")
    suspend fun checkDevice(
        @Field("qrcode") qrcode: String?
    ): CheckDeviceModel

    @Multipart
    @POST("api/image/v1/upload")
    fun imageUpload(

        @Part("qrcode") qrcode: RequestBody,
        @Part images: List<MultipartBody.Part>

    ): UploadImageModel

}