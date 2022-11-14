package space.timur.tradestatandroid.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DescriptionViewModel: ViewModel() {

    private val descEventChannel = Channel<DescEvent>()
    val descEvent = descEventChannel.receiveAsFlow()

    sealed class DescEvent {
        object NavigateToWelcomeScreen : DescEvent()
    }

    fun onButtonNextClick() = viewModelScope.launch {
        descEventChannel.send(DescEvent.NavigateToWelcomeScreen)
    }
}