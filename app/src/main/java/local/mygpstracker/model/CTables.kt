package local.mygpstracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coordinates")
class Coordinates(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "event_date") val event_date: String?,
    @ColumnInfo(name = "latitude") val latitude: Float?,
    @ColumnInfo(name = "longitude") val longitude: Float?
)

data class GpsInfo (
    @SerializedName("device") val device: Int?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)