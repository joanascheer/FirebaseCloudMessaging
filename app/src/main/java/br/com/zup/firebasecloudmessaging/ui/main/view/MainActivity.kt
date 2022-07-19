package br.com.zup.firebasecloudmessaging.ui.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.com.zup.firebasecloudmessaging.databinding.ActivityMainBinding
import br.com.zup.firebasecloudmessaging.domain.MyFirebaseMessagingService
import br.com.zup.firebasecloudmessaging.domain.MyFirebaseMessagingService.Companion.TAG
import br.com.zup.firebasecloudmessaging.domain.model.Message
import br.com.zup.firebasecloudmessaging.ui.main.viewmodel.MainViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.extras?.getString("message")
            val title = intent.extras?.getString("title")
            viewModel.updateMessage(Message(title,message))
//            binding.tvMsgTextValue.text = message
//            binding.tvMsgTitleValue.text = title

            //getMessage(Message(title,message))
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        /* Criando uma instancia do local briadcast manager pra checar dados com a label my data, que
        * eu setei lá em MyFirebaseMessagingService */
       LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("Mensagem"))
       LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("Título"))

    }

    override fun onStop() {
        super.onStop()
        /* Apagar registro pra evitar manter dados desnecessários na memória */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGetToken.setOnClickListener {
            if (checkGooglePlayServices()) {

            } else {
                Log.w(TAG, "Este emulador não pode receber mensagem Fe, troca.")
            }
            getCurrentMessagingToken()
        }
        initObservers()


    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Não vai dar pra receber mensagens neste emulador. Faça update do google services ou troque de emulador")
            false
        } else {
            Log.i(TAG, "Google play services updated")
            true
        }
    }

    private fun initObservers() {
//        viewModel.tokenResponse.observe(this) {
//            getCurrentMessagingToken()
//        }
        viewModel.messageResponse.observe(this) {
            getMessage(Message(it.title, it.body))
        }
    }



    private fun getToken(token: String) {
        binding.tvTokenValue.text = token
    }

    private fun getMessage(message: Message) {

        binding.tvMsgTitleValue.text = message.title
        binding.tvMsgTextValue.text = message.body

    }

    private fun getCurrentMessagingToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                viewModel.updateToken(token)
                getToken(token)
                Log.d(MyFirebaseMessagingService.TAG, "Token: $token")
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            }
        )

    }
}