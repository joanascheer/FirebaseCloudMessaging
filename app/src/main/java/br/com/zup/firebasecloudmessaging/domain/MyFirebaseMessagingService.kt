package br.com.zup.firebasecloudmessaging.domain

import android.content.Intent
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

//    override fun onNewToken(token: String) {
//        viewModel.updateToken(token)
//        //viewmodel chamando a funcao que eu fiz la
//     }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        handleMessage(remoteMessage)
//        remoteMessage.notification?.let {
//            val message = Message(title = it.title.toString(), body = it.body.toString())
//            viewModel.updateMessage(message)
//        }



        //no vm uma funcao que recebe essa msg e faz o mesmo que com o token pra atualizar e observar na activity

        //no vm funcao pra atualizar corpo e titulo msg. recebe token e atualiza priv val token. mesmo com msg
        //chamar no vm que recebe message e atualiza ok
        //activity os observers da msg e token ok
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
//You create a handler to get the message and extract the data.
        val handler = Handler(Looper.getMainLooper())

        //You use that handler to post the toast through a runnable.
        handler.post(Runnable {
            Toast.makeText(baseContext, getString(R.string.handle_notification_now),
                Toast.LENGTH_LONG).show()
            remoteMessage.notification?.let {
                val intentBodyMsg = Intent("Mensagem")
                intentBodyMsg.putExtra("message", it.body)
                val intentTitleMsg = Intent ("Título")
                intentTitleMsg.putExtra("title", it.title )
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