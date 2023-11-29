package com.flash.inventory.ui.screens.listDevices

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.flash.inventory.data.network.Resource
import com.flash.inventory.databinding.ActivityListDevicesBinding
import com.flash.inventory.ui.screens.imageUpload.UploadImageActivity
import com.flash.inventory.ui.viewModels.CommonViewModel
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDevicesActivity : AppCompatActivity() {

    lateinit var binding: ActivityListDevicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDevicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listDevicesScanQr.setOnClickListener {

            val integrator = IntentIntegrator(this)
            integrator.setOrientationLocked(true)
            integrator.setPrompt("Scan QR code")
            integrator.setBeepEnabled(true) //Use this to set whether you need a beep sound when the QR code is scanned
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.initiateScan()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {


                val i = Intent(this, UploadImageActivity::class.java)
                i.putExtra("qrString", result.contents.toString())
                startActivity(i)

//                var fragment =
//                    supportFragmentManager.findFragmentByTag(TabFragment::class.java.simpleName) as TabFragment
//                fragment.passDevice(result.contents)

            }
        }

    }

}