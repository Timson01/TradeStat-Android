package space.timur.tradestatandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val logInEventChannel = Channel<LogInEvent>()
    val logInEvent = logInEventChannel.receiveAsFlow()

    sealed class LogInEvent {
        object NavigateToDealsScreen : LogInEvent()
        object NavigateToRegisterScreen : LogInEvent()
    }

    fun onButtonLogInClick() = viewModelScope.launch {
        logInEventChannel.send(LogInEvent.NavigateToDealsScreen)
    }

    fun onButtonSignUpClick() = viewModelScope.launch {
        logInEventChannel.send(LogInEvent.NavigateToRegisterScreen)
    }
}