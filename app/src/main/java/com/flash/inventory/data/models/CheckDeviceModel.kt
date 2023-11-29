package com.flash.inventory.data.models

import com.google.zxing.qrcode.encoder.QRCode

data class CheckDeviceModel(

    var data: CheckDeviceData? = null,
    var err: String? = null,
    var msg: String? = null

)

data class CheckDeviceData(

    var qrCode: String?,
    var images: ArrayList<CheckDeviceImages>? = arrayListOf()

)

data class CheckDeviceImages(

    var name: String,
    var data: String,

)

//data class CheckDeviceError(){
//
//
//
//}
//    {
//        "data": {
//        "qrCode": "24rdyfo",
//        "images": [
//        {
//            "name": "xxxx1",
//            "data": "bas64/xxxxxxx"
//        },
//        {
//            "name": "xxxx1",
//            "data": "bas64/xxxxxxx"
//        },
//        {
//            "name": "xxxx1",
//            "data": "bas64/xxxxxxx"
//        }
//        ]
//    },
//        "err":null,
//        "msg":"successful"
//    }
