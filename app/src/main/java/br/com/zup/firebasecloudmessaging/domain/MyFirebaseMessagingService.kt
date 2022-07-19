package br.com.zup.firebasecloudmessaging.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.com.zup.firebasecloudmessaging.R
import br.com.zup.firebasecloudmessaging.domain.model.Message
import br.com.zup.firebasecloudmessaging.ui.main.viewmodel.MainViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val viewModel = MainViewModel()
    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        viewModel.updateToken(token)
     }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        //Pegando a mensagem e extraindo os dados dela
        val handler = Handler(Looper.getMainLooper())

        //lidando com a mensagem quando o app está em primeiro plano
        handler.post(Runnable {
            Toast.makeText(
                baseContext, getString(R.string.handle_notification_now),
                Toast.LENGTH_LONG
            ).show()
            remoteMessage.notification?.let {
                val intentBodyMsg = Intent("Mensagem")
                intentBodyMsg.putExtra("message", it.body)
                val intentTitleMsg = Intent("Título")
                intentTitleMsg.putExtra("title", it.title)
                broadcaster?.sendBroadcast(intentBodyMsg)
                broadcaster?.sendBroadcast(intentTitleMsg)
            }
        }

        )
    }

    companion object {
        const val TAG = "TesteFirebase"
    }


}