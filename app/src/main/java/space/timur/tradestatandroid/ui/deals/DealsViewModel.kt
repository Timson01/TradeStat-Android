package space.timur.tradestatandroid.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.data.DealDao
import space.timur.tradestatandroid.util.Quadruple
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val dealDao: DealDao
    ): ViewModel() {

    val defaultDeals = dealDao.getDealsByDefault("").asLiveData()
    private val dealsEventChannel = Channel<DealsEvent>()
    val dealsEvent = dealsEventChannel.receiveAsFlow()

    val searchQuery = MutableStateFlow("")
    val startDate = MutableStateFlow<Long>(0)
    val endDate = MutableStateFlow<Long>(0)
    val sortOrder = MutableStateFlow(SortOrder.BY_DEFAULT)

    private val dealFlow = combine(
        searchQuery,
        sortOrder,
        startDate,
        endDate
    ) {
        query, sortOrder, startDate, endDate ->
        Quadruple(query, sortOrder, startDate, endDate)
    }.flatMapLatest { (query, sortOrder, startDate, endDate) ->
        dealDao.getDeals(query, sortOrder, startDate, endDate)
    }

    val deals = dealFlow.asLiveData()

    fun onButtonAddDealClick() = viewModelScope.launch {
        dealsEventChannel.send(DealsEvent.NavigateToAddDealsScreen)
    }

    sealed class DealsEvent {
        object NavigateToAddDealsScreen : DealsEvent()
    }
}
enum class SortOrder {BY_POSITIVE_INCOME, BY_NEGATIVE_INCOME, BY_NAME_AND_DATE, BY_DEFAULT}
