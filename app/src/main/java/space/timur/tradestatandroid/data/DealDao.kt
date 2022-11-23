package space.timur.tradestatandroid.data

import androidx.room.*

@Dao
interface DealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deal: Deal)

    @Update
    suspend fun update(deal: Deal)

    @Delete
    suspend fun delete(deal: Deal)

    @Query("SELECT * FROM deal_table")
    suspend fun deleteCompletedTasks()

}