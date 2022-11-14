package space.timur.tradestatandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val logInEventChannel = Channel<LogInEvent>()
    val descEvent = logInEventChannel.receiveAsFlow()

    sealed class LogInEvent {
        object NavigateToWelcomeScreen : LogInEvent()
    }

    fun onButtonNextClick() = viewModelScope.launch {
        logInEventChannel.send(LogInEvent.NavigateToWelcomeScreen)
    }
}