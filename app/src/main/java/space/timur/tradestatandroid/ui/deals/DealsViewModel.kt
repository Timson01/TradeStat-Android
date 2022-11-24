package space.timur.tradestatandroid.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.data.DealDao
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val dealDao: DealDao
    ): ViewModel() {

    private val dealsEventChannel = Channel<DealsEvent>()
    val dealsEvent = dealsEventChannel.receiveAsFlow()
    val deals = dealDao.getDeals().asLiveData()

    fun onButtonAddDealClick() = viewModelScope.launch {
        dealsEventChannel.send(DealsEvent.NavigateToAddDealsScreen)
    }

    sealed class DealsEvent {
        object NavigateToAddDealsScreen : DealsEvent()
    }

}