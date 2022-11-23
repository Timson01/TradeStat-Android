package space.timur.tradestatandroid.data

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

@Entity(tableName = "deal_table")
@Parcelize
data class Deal(
    val tickerName: String,
    val description: String = "You didn't write anything here",
    val created: Long = System.currentTimeMillis(),
    val hashtag: String = "You didn't write anything here",
    val imagePath: String = "",
    val amount: Double = 0.0,
    var numberOfStocks: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        @SuppressLint("SimpleDateFormat")
        get() = SimpleDateFormat("dd/MM/yy HH:mm").format(created)
}
