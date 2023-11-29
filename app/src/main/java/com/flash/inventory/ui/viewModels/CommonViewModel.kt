package com.flash.inventory.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flash.inventory.data.models.CheckDeviceModel
import com.flash.inventory.data.models.UploadImageModel
import com.flash.inventory.data.network.CreateRequest
import com.flash.inventory.data.network.Resource
import com.flash.inventory.data.repository.CommonRepo
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val repository: CommonRepo) : ViewModel() {

    private val _deviceResponse: MutableLiveData<Resource<CheckDeviceModel>> = MutableLiveData()
    val deviceResponse: LiveData<Resource<CheckDeviceModel>>
        get() = _deviceResponse

    private val _uploadResponse: MutableLiveData<Resource<UploadImageModel>> = MutableLiveData()
    val uploadResponse: LiveData<Resource<UploadImageModel>>
        get() = _uploadResponse


    fun checkDeviceVM(qrString: String) = viewModelScope.launch {

        _deviceResponse.value = Resource.Loading
        _deviceResponse.value = repository.doCheckDevice(qrString)
    }

    fun uploadDeviceVM(qrStringRB: RequestBody, arrayList: ArrayList<MultipartBody.Part>) = viewModelScope.launch {
        println("..............................................")
        println(qrStringRB.contentLength())
        println(arrayList.size)
        _uploadResponse.value = Resource.Loading
        _uploadResponse.value = repository.doUploadImages(qrStringRB, arrayList)
    }

}