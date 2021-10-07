package com.edu.walletdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.coinbase.wallet.core.*
import com.coinbase.walletlink.WalletLink
import com.coinbase.walletlink.models.ClientMetadataKey
import com.coinbase.walletlink.models.HostRequestId
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import java.net.URL
import java.util.*
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class MainActivity : AppCompatActivity() {
    private var btn: Button? = null
    private var tvresult: TextView? = null
    private var walletLink : WalletLink? = null
    private val disposeBag = CompositeDisposable()

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
        initWalletLink(text)
    }

    private fun redirection(text: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(text)
        startActivity(openURL)
    }

    // To pair the device with a browser after scanning WalletLink QR code
    private fun initWalletLink(text: String) {
        walletLink = WalletLink(notificationUrl = URL(text), applicationContext)
        val sessionId = ""
        val secret = ""
        val serverUrl = URL(text)
        val userId = ""
        val primaryAddress = ""
        val version =""
        walletLink!!.link(
            sessionId = sessionId,
            secret = secret,
            url = serverUrl,
            userId = userId,
            version = version,
            metadata = mapOf(ClientMetadataKey.EthereumAddress to primaryAddress)
        ).subscribeBy(onSuccess = {
            print("New WalletLink connection was established")
        }, onError = { error ->
            print("Error while connecting to WalletLink server (walletlink)")
        }).addTo(disposeBag)
    }

//    // Listen on incoming requests
//    private fun listen () {
//        // Listen on incoming requests
//        walletLink.requests
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(onNext = { request ->
//                // New unseen request
//            })
//            .addTo(disposeBag)
//    }
//
//    // Approve DApp permission request (EIP-1102)
//    private fun approveRequest (hostRequestId : HostRequestId) {
//        walletLink.approveDappPermission(hostRequestId)
//        .subscribeBy(onSuccess = {
//            // Browser received EIP-1102 approval
//        }, onError = { error ->
//            // Browser failed to receive EIP-1102 approval
//        }).addTo(disposeBag)
//    }

    // Approve a given transaction/message signing request
    private fun approveRequest (hostRequestId : HostRequestId, signedData : ByteArray) {
        walletLink!!.approve(hostRequestId, signedData)
            .subscribeBy(onSuccess = {
                print("Received request approval")
            }, onError = { error ->
                print("Failed to receive request approval")
            })
            .addTo(disposeBag)
    }

    // Reject transaction/message/EIP-1102 request
    private fun rejectRequest (hostRequestId : HostRequestId, signedData : ByteArray) {
        walletLink!!.approve(hostRequestId, signedData)
            .subscribeBy(onSuccess = {
                print("Received request approval")
            }, onError = { error ->
                print("Failed to receive request approval")
            })
            .addTo(disposeBag)
    }

}
