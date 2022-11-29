package space.timur.tradestatandroid.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val TAG = "PreferencesManager"

enum class SortOrder {BY_POSITIVE_INCOME, BY_NEGATIVE_INCOME, BY_NAME_AND_DATE, BY_DEFAULT}

data class FilterPreferences (val sortOrder: SortOrder, val startDate: Long, val endDate: Long)

class PreferencesManager @Inject constructor(@ApplicationContext context: Context){

    private val dataStore = context.createDataStore("user_preferences")

    val preferenceFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferenceKeys.SORT_ORDER] ?: SortOrder.BY_DEFAULT.name
            )
            val startDate = preferences[PreferenceKeys.START_DATE] ?: 0
            val endDate = preferences[PreferenceKeys.END_DATE] ?: 0
            FilterPreferences(sortOrder, startDate, endDate)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateStartDate (startDate: Long) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.START_DATE] = startDate
        }
    }

    suspend fun updateEndDate (endDate: Long) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.END_DATE] = endDate
        }
    }

    private object PreferenceKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val START_DATE = preferencesKey<Long>("start_date")
        val END_DATE = preferencesKey<Long>("end_date")
    }

}