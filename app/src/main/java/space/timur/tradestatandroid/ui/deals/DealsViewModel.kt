package space.timur.tradestatandroid.ui.deals

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.ADD_TASK_RESULT_OK
import space.timur.tradestatandroid.EDIT_TASK_RESULT_OK
import space.timur.tradestatandroid.data.Deal
import space.timur.tradestatandroid.data.DealDao
import space.timur.tradestatandroid.data.PreferencesManager
import space.timur.tradestatandroid.data.SortOrder


class DealsViewModel @ViewModelInject constructor(
    private val dealDao: DealDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
    ): ViewModel() {

    val defaultDeals = dealDao.getDealsByDefault("").asLiveData()
    private val dealsEventChannel = Channel<DealsEvent>()
    val dealsEvent = dealsEventChannel.receiveAsFlow()

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferenceFlow

    private val dealFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) {
            query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        dealDao.getDeals(query, filterPreferences.sortOrder, filterPreferences.startDate, filterPreferences.endDate)
    }

    val deals = dealFlow.asLiveData()

    fun onButtonAddDealClick() = viewModelScope.launch {
        dealsEventChannel.send(DealsEvent.NavigateToAddDealsScreen)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onStartDateSelected(startDate: Long) = viewModelScope.launch {
        preferencesManager.updateStartDate(startDate)
    }

    fun onEndDateSelected(endDate: Long) = viewModelScope.launch {
        preferencesManager.updateEndDate(endDate)
    }

    fun onDealSwiped(deal: Deal) = viewModelScope.launch {
        dealDao.delete(deal)
        dealsEventChannel.send(DealsEvent.ShowUndoDeleteMessage(deal))
    }

    fun onUndoDeleteClick(deal: Deal) = viewModelScope.launch {
        dealDao.insert(deal)
    }

    fun onDealSelected(deal: Deal) = viewModelScope.launch {

    }

    fun onDealLongClick(deal: Deal) = viewModelScope.launch {
        dealsEventChannel.send(DealsEvent.NavigateToEditDealsScreen(deal))
    }

    fun onAddEditResult(result: Int){
        when(result){
            ADD_TASK_RESULT_OK -> showDealSavedConfirmationMessage("Deal added")
            EDIT_TASK_RESULT_OK -> showDealSavedConfirmationMessage("Deal updated")
        }
    }

    private fun showDealSavedConfirmationMessage(text: String) = viewModelScope.launch {
        dealsEventChannel.send(DealsEvent.ShowDealSavedConfirmationMessage(text))
    }

    sealed class DealsEvent {
        data class NavigateToEditDealsScreen(val deal: Deal) : DealsEvent()
        object NavigateToAddDealsScreen : DealsEvent()
        data class ShowUndoDeleteMessage(val deal: Deal) : DealsEvent()
        data class ShowDealSavedConfirmationMessage(val msg: String) : DealsEvent()
    }
}

