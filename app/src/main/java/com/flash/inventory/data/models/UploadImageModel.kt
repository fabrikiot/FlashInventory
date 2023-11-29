package com.flash.inventory.data.models

data class UploadImageModel(
    var data:ArrayList<String>? = null,
    var err: UploadImageError? = null,
    var msg: String? = null

)

data class UploadImageError(

    var errcode: String? = null

)