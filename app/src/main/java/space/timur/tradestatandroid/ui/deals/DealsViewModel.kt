package space.timur.tradestatandroid.ui.deals

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.timur.tradestatandroid.data.DealDao
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val taskDao: DealDao
    ): ViewModel() {

}