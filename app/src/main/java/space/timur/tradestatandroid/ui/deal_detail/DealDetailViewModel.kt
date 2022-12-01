package space.timur.tradestatandroid.ui.deal_detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.ui.add_edit_deal.AddEditDealViewModel

class DealDetailViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val deal =  state.get<Deal>("deal")

    var dealName = state.get<String>("dealName") ?: deal?.tickerName ?: ""
        set(value) {
            field = value
            state.set("dealName",  value)
        }

    var dealDescription = state.get<String>("dealDescription") ?: deal?.description ?: ""
        set(value) {
            field = value
            state.set("dealDescription",  value)
        }

    var dealCreated = state.get<Long>("dealCreated") ?: deal?.created ?: 0L
        set(value) {
            field = value
            state.set("dealCreated",  value)
        }

    var dealHashtag = state.get<String>("dealHashtag") ?: deal?.hashtag ?: ""
        set(value) {
            field = value
            state.set("dealHashtag",  value)
        }

    var dealImagePath = state.get<String>("dealImagePath") ?: deal?.imagePath ?: ""
        set(value) {
            field = value
            state.set("dealImagePath",  value)
        }

    var dealAmount = state.get<Double>("dealAmount") ?: deal?.amount ?: 0.0
        set(value) {
            field = value
            state.set("dealAmount",  value)
        }

    var dealNumberOfStocks = state.get<Int>("dealNumberOfStocks") ?: deal?.numberOfStocks ?: 0
        set(value) {
            field = value
            state.set("dealNumberOfStocks",  value)
        }

    private val dealDetailEventChannel = Channel<DealDetailViewModel.DealDetailEvent>()
    val dealDetailEvent = dealDetailEventChannel.receiveAsFlow()

    fun onBackClick() = viewModelScope.launch {
        dealDetailEventChannel.send(DealDetailViewModel.DealDetailEvent.NavigateBack)
    }

    sealed class DealDetailEvent {
        object NavigateBack : DealDetailEvent()
    }
}