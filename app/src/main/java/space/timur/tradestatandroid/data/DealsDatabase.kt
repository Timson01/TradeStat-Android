package space.timur.tradestatandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import space.timur.tradestatandroid.di.ApplicationScope
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Deal::class], version = 1)
abstract class DealDatabase : RoomDatabase() {

    abstract fun dealDao(): DealDao

    class Callback @Inject constructor(
        private val database: Provider<DealDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().dealDao()
            val myDate = "2014/10/29 18:10:45"
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date: Date = sdf.parse(myDate)
            val millis: Long = date.getTime()
            applicationScope.launch {
                dao.insert(
                    Deal(
                        tickerName = "UTC",
                        description = "It's the first deal, which has been added to TradeStat application",
                        created = millis,
                        amount = 20.00,
                        )
                )
                dao.insert(
                    Deal(
                        tickerName = "FTC",
                        description = "It's the second deal, which has been added to TradeStat application",
                        amount = -31.10,
                    )
                )
                dao.insert(
                    Deal(
                        tickerName = "UTC",
                        description = "It's the third deal, which has been added to TradeStat application",
                        created = millis,
                        amount = 15.00,
                    )
                )
                dao.insert(
                    Deal(
                        tickerName = "UTC",
                        description = "It's the fourth deal, which has been added to TradeStat application",
                        amount = 17.80,
                    )
                )
                dao.insert(
                    Deal(
                        tickerName = "UTC",
                        description = "It's the fifth deal, which has been added to TradeStat application",
                        created = millis,
                        amount = -18.13,
                    )
                )
            }
        }
    }

}