package com.flash.inventory.ui.screens.imageUpload

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.flash.inventory.R
import com.flash.inventory.adapter.SelfieRecyclerAdapter
import com.flash.inventory.adapter.ViewPagerAdapter
import com.flash.inventory.data.models.UploadImageModel
import com.flash.inventory.data.network.Resource
import com.flash.inventory.databinding.ActivityUploadImageBinding
import com.flash.inventory.extras.RetrofitClient
import com.flash.inventory.ui.screens.listDevices.ListDevicesActivity
import com.flash.inventory.ui.viewModels.CommonViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class UploadImageActivity : AppCompatActivity(), SelfieRecyclerAdapter.onClickItem {

    lateinit var binding: ActivityUploadImageBinding
    val mViewModel by viewModels<CommonViewModel>()

    var qrstring = ""

    lateinit var RecyclerViewLayoutManager: RecyclerView.LayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: SelfieRecyclerAdapter
    lateinit var dataArray: ArrayList<String>
    lateinit var dataFileArray: ArrayList<File>

    //For data from api
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList: ArrayList<String>

    lateinit var progressDialog: ProgressDialog

    val PIC_CAMERA = 2404
    val PIC_CROP = 2

    private lateinit var photoFile: File
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        progressDialog.show()
        val extras = intent.extras
        if (extras != null) {
            qrstring = extras.getString("qrString") as String
        }
        println("QRString:: $qrstring")

        mViewModel.checkDeviceVM(qrstring)

        binding.uploadImageQrString.text = qrstring

        mViewModel.deviceResponse.observe(this) {

            progressDialog.cancel()

            when (it) {

                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Success -> {
                    progressDialog.cancel()
                    if (it.value.data != null) {
                        if (it.value.data?.images?.size == 0) {

                            binding.idViewPager.visibility = View.GONE
                            binding.tabDots.visibility = View.GONE

                        } else {

                            binding.idViewPager.visibility = View.VISIBLE
                            binding.tabDots.visibility = View.VISIBLE

                            for (i in 0 until (it.value.data?.images?.size ?: 0)) {
                                it.value.data?.images?.get(i)?.data?.let { it1 -> imageList.add(it1) }
                                viewPagerAdapter.notifyDataSetChanged()
                            }

                        }
                    } else {
//                        Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show()
                    }

                }
                is Resource.Failure -> {

                    Toast.makeText(this, "" + it.errorCode, Toast.LENGTH_SHORT).show()

                }

                else -> {}
            }

        }

//        mViewModel.uploadResponse.observe(this) {
//
//
//            when (it) {
//
//                is Resource.Success -> {
//
//                    if (it.value.err == null) {
//                        Toast.makeText(this, it.value.msg, Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, it.value.err?.errcode, Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//                is Resource.Failure -> {
//
//                }
//
//                else -> {}
//            }
//
//        }

        binding.uploadImagesUpload.setOnClickListener {

            if (dataFileArray.size > 0) {
//                alertDialog()
                uploadImage()
            } else {
                Toast.makeText(this, " Add image to upload", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadImage() {

        progressDialog.show()

        val qrString: RequestBody = qrstring.toRequestBody("text/plain".toMediaTypeOrNull())
        val multiPartArray: ArrayList<MultipartBody.Part> = ArrayList()
        for (i in 0 until dataFileArray.size) {
            multiPartArray.add(prepareFilePart("images", dataFileArray[i]))
        }

        RetrofitClient.instance.imageUpload(qrString, multiPartArray)
            .enqueue(object : Callback<UploadImageModel> {
                override fun onResponse(
                    call: Call<UploadImageModel>?,
                    response: Response<UploadImageModel>
                ) {
                    println(response.body()?.msg)

                    progressDialog.cancel()

                    if (response.body()?.err == null) {

                        var i = Intent(this@UploadImageActivity, ListDevicesActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(i)
                        finish()

                    }
                }

                override fun onFailure(call: Call<UploadImageModel>?, t: Throwable?) {

                    progressDialog.cancel()
                    println(t?.printStackTrace())

                }

            })

//        mViewModel.uploadDeviceVM(qrString, multiPartArray)

    }

    private fun prepareFilePart(name: String, file: File): MultipartBody.Part {

//        val file: File = fileUri.toFile()
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaType())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }


    private fun initialize() {

        progressDialog = ProgressDialog(this@UploadImageActivity)
        progressDialog.setTitle(null)
        progressDialog.setMessage("Please wait, while loading")
        progressDialog.setCancelable(false)


        dataArray = ArrayList()
        dataArray.add("1")

        dataFileArray = ArrayList()

        recyclerView = binding.attachPhotosRecyclerView

        RecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = RecyclerViewLayoutManager
        (RecyclerViewLayoutManager as LinearLayoutManager).reverseLayout = true
        (RecyclerViewLayoutManager as LinearLayoutManager).stackFromEnd = true
        recyclerViewAdapter = SelfieRecyclerAdapter(this, dataArray, this)
        recyclerView.adapter = recyclerViewAdapter


        viewPager = binding.idViewPager
        imageList = ArrayList<String>()
        viewPagerAdapter = ViewPagerAdapter(this@UploadImageActivity, imageList)
        viewPager.adapter = viewPagerAdapter

        val tabLayout = binding.tabDots
        tabLayout.setupWithViewPager(viewPager, true)

    }

    override fun onClick() {

        ImagePicker.with(this)
            .cameraOnly()
            .cropSquare()
            .start()

//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        photoFile = randomFile()
//        println("..................x"+photoFile)
//        photoUri = FileProvider.getUriForFile(this, "com.flash.inventory", photoFile)
//        println(".................."+photoUri)
//        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        startActivityForResult(cameraIntent, PIC_CAMERA)

    }

    override fun onClickCross(pos: Int) {

        dataArray.removeAt(pos)
        recyclerViewAdapter.notifyDataSetChanged()

    }


    @SuppressLint("NewApi")
    private fun randomFile(): File {
        var formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val now = LocalDate.now()
        return File(applicationContext?.filesDir, "pic-${UUID.randomUUID()}-${now.format(formatter)}.jpg")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("RequestCode:::$requestCode")
        if (resultCode == RESULT_OK && requestCode == PIC_CAMERA) {

//            println("FileUri:::::::::::::::::::::::::::::::::${photoUri.path}")
//            photoUri?.let { performCrop(it) }

            val fileUri = data?.data
            dataArray.add(fileUri.toString())
            recyclerViewAdapter.notifyDataSetChanged()

            println("Image Name::: ${getFileName(fileUri!!)}")

            val file = File(fileUri.path.toString())
            dataFileArray.add(file)


//            val filePath: String? = ImagePicker.getFilePath(data)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }




        if (requestCode == PIC_CROP) {
//            if (data != null) {
//                val fileUri = data.data
//                dataArray.add(fileUri.toString())
//                recyclerViewAdapter.notifyDataSetChanged()
//
//                println("Image Name::: ${getFileName(fileUri!!)}")
//
//                val file = File(fileUri.path.toString())
//                dataFileArray.add(file)
//            }
        }

    }

    @SuppressLint("Range")
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }


    private fun alertDialog() {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.alert_diag, null);
        dialogBuilder.setView(dialogView);
        val alertDialog = dialogBuilder.create()

        val okBtn: AppCompatButton = dialogView.findViewById<AppCompatButton>(R.id.alertOK)
        val cancelBtn: AppCompatButton = dialogView.findViewById<AppCompatButton>(R.id.alertCancel)
        okBtn.setOnClickListener { uploadImage() }
        cancelBtn.setOnClickListener { alertDialog.cancel() }
        alertDialog.show()

    }

    private fun performCrop(picUri: Uri) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties here
            cropIntent.putExtra("crop", true)
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128)
            cropIntent.putExtra("outputY", 128)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP)
        } // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            // display an error message
            val errorMessage = "Whoops - your device doesn't support the crop action!"
            val toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
            toast.show()
        }
    }


}