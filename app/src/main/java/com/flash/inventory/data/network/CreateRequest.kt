package com.flash.inventory.data.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*


object CreateRequest {

    fun getCheckDeviceData(qrCodeString: String?): RequestBody? {
        val obj = JSONObject().put("qrcode", qrCodeString)
        return obj.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}
