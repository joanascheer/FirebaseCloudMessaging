package br.com.zup.firebasecloudmessaging.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.zup.firebasecloudmessaging.domain.model.Message

class MainViewModel : ViewModel() {

    private val _tokenResponse = MutableLiveData<String>()
    var tokenResponse: LiveData<String> = _tokenResponse

    private val _messageResponse = MutableLiveData<Message>()
    var messageResponse = _messageResponse

    fun updateToken(token:String) {
         _tokenResponse.value = token
    }

    fun updateMessage(message: Message){
        _messageResponse.value = Message(title = message.title, body = message.body)

    }

}