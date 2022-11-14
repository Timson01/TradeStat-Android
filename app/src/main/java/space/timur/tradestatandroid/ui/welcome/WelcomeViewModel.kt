package space.timur.tradestatandroid.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WelcomeViewModel: ViewModel() {

    private val welcomeEventChannel = Channel<WelcomeEvent>()
    val welcomeEvent = welcomeEventChannel.receiveAsFlow()

    sealed class WelcomeEvent {
        object NavigateToRegisterScreen : WelcomeEvent()
        object NavigateToLogInScreen : WelcomeEvent()
    }

    fun onButtonSignUpClick() = viewModelScope.launch {
        welcomeEventChannel.send(WelcomeEvent.NavigateToRegisterScreen)
    }

    fun onButtonSignInGoogleClick() = viewModelScope.launch {
        welcomeEventChannel.send(WelcomeEvent.NavigateToRegisterScreen)
    }

    fun onButtonLogInClick() = viewModelScope.launch {
        welcomeEventChannel.send(WelcomeEvent.NavigateToLogInScreen)
    }
}