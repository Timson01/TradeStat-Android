package space.timur.tradestatandroid.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deal: Deal)

    @Update
    suspend fun update(deal: Deal)

    @Delete
    suspend fun delete(deal: Deal)

    @Query("SELECT * FROM deal_table")
    fun getDeals(): Flow<List<Deal>>

}