package space.timur.tradestatandroid.ui.add_edit_deal

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.ADD_TASK_RESULT_OK
import space.timur.tradestatandroid.EDIT_TASK_RESULT_OK
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.data.DealDao

class AddEditDealViewModel @ViewModelInject constructor(
    private val dealDao: DealDao,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val deal =  state.get<Deal>("deal")
    var fragmentTitle = if(deal != null){
        "Update your Deal,"
    }else{
        "Create your Deal,"
    }

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

    var dealCreated = state.get<String>("dealCreated") ?: deal?.createdDateFormatted ?: ""
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

    private val addEditDealEventChannel = Channel<AddEditDealEvent>()
    val addEditDealEvent = addEditDealEventChannel.receiveAsFlow()

    fun onSaveClick(){
        if (dealName.isBlank() && dealCreated.isNotBlank()) {
            // show invalid input message
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (dealCreated.isBlank() && dealCreated.isNotBlank()){
            showInvalidInputMessage("Deal created cannot be empty")
            return
        }
        if(dealName.isBlank() && dealCreated.isBlank()) {
            showInvalidInputMessage("Deal name and created cannot be empty")
            return
        }
        if(deal != null){
            val updatedDeal = deal.copy(tickerName = dealName, description = dealDescription,
                created = deal.created, hashtag = dealHashtag, imagePath = dealImagePath,
            amount = dealAmount, numberOfStocks = dealNumberOfStocks)
            updateDeal(updatedDeal)
        }else{
            val newDeal = Deal(tickerName = dealName, description = dealDescription,
                created = deal?.created ?: 0, hashtag = dealHashtag, imagePath = dealImagePath,
                amount = dealAmount, numberOfStocks = dealNumberOfStocks)
            createDeal(newDeal)
        }
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditDealEventChannel.send(AddEditDealEvent.ShowInvalidInputMessage(text))
    }

    private fun createDeal(deal: Deal) = viewModelScope.launch {
        dealDao.insert(deal)
        addEditDealEventChannel.send(AddEditDealEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateDeal(deal: Deal) = viewModelScope.launch {
        dealDao.update(deal)
        addEditDealEventChannel.send(AddEditDealEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    fun onBackClick() = viewModelScope.launch {
        addEditDealEventChannel.send(AddEditDealEvent.NavigateBack)
    }

    sealed class AddEditDealEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditDealEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditDealEvent()
        object NavigateBack : AddEditDealEvent()
    }
}