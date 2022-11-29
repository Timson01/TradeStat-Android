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

    fun getDeals(query: String, sortOrder: SortOrder, startDate: Long, endDate: Long): Flow<List<Deal>> =
        when(sortOrder){
            SortOrder.BY_NEGATIVE_INCOME -> getNegativeDeals(query, startDate, endDate)
            SortOrder.BY_NAME_AND_DATE -> getDealsByNameAndDate(query,startDate,endDate)
            SortOrder.BY_POSITIVE_INCOME -> getPositiveDeals(query,startDate, endDate)
            SortOrder.BY_DEFAULT -> getDealsByDefault(query)
        }

    @Query("SELECT * FROM deal_table WHERE created BETWEEN " +
            ":startDate AND :endDate " +
            "AND tickerName LIKE '%' || :searchQuery || '%' " +
            "ORDER BY created DESC")
    fun getDealsByNameAndDate(searchQuery: String, startDate: Long, endDate: Long): Flow<List<Deal>>

    @Query("SELECT * FROM deal_table WHERE tickerName LIKE '%' || :searchQuery || '%' ORDER BY created DESC")
    fun getDealsByDefault(searchQuery: String): Flow<List<Deal>>

    @Query("SELECT * FROM deal_table " +
            "WHERE amount >= 0 AND created BETWEEN :startDate AND :endDate " +
            "AND tickerName LIKE '%' || :searchQuery || '%' ORDER BY created DESC")
    fun getPositiveDeals(searchQuery: String, startDate: Long, endDate: Long): Flow<List<Deal>>

    @Query("SELECT * FROM deal_table WHERE created BETWEEN " +
            ":startDate AND :endDate " +
            "AND amount <= 0.0 AND tickerName LIKE '%' || :searchQuery || '%' " +
            "ORDER BY created DESC")
    fun getNegativeDeals(searchQuery: String, startDate: Long, endDate: Long): Flow<List<Deal>>

}