package com.flash.inventory.data.repository

import com.flash.inventory.data.network.ApiInterface
import com.flash.inventory.data.network.SafeApiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CommonRepo @Inject constructor(private val api: ApiInterface): SafeApiCall {

    suspend fun doCheckDevice(qrData: String?) = safeApiCall{

        api.checkDevice(qrData)

    }

    suspend fun doUploadImages(body: RequestBody, arrayList: ArrayList<MultipartBody.Part>) = safeApiCall{

        api.imageUpload(body, arrayList)

    }

//    suspend fun getMyVds(body: RequestBody?) = safeApiCall {
//
//        api.getMyVdsNew(body)
//
//    }
}