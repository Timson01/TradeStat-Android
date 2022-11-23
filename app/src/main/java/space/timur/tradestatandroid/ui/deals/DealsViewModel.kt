package space.timur.tradestatandroid.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import space.timur.tradestatandroid.data.DealDao
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val dealDao: DealDao
    ): ViewModel() {

        val deals = dealDao.getDeals().asLiveData()

}