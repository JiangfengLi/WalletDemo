package com.edu.walletdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

class MainActivity : AppCompatActivity() {
    private var btn: Button? = null
    private var tvresult: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val scanQrCode = registerForActivityResult(ScanQRCode(), ::handleResult)

        tvresult = findViewById<View>(R.id.tvresult) as TextView
        btn = findViewById<View>(R.id.btn) as Button
        btn!!.setOnClickListener {
            scanQrCode.launch(null)
        }
    }

    private fun handleResult(result: QRResult) {
        val text = when (result) {
            is QRResult.QRSuccess -> result.content.rawValue
            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }
        tvresult!!.text = text
        redirection(text)
    }

    private fun redirection (text : String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(text)
        startActivity(openURL)
    }
}
