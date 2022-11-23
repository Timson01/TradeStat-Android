package space.timur.tradestatandroid.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val registerEventChannel = Channel<LogInEvent>()
    val registerEvent = registerEventChannel.receiveAsFlow()

    sealed class LogInEvent {
        object NavigateToDealsScreen : LogInEvent()
        object NavigateToLogInScreen : LogInEvent()
    }

    fun onButtonLogInClick() = viewModelScope.launch {
        registerEventChannel.send(LogInEvent.NavigateToLogInScreen)
    }

    fun onButtonRegisterClick() = viewModelScope.launch {
        registerEventChannel.send(LogInEvent.NavigateToDealsScreen)
    }

}