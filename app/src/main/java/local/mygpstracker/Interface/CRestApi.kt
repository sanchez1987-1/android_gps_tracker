package local.mygpstracker.Interface
import local.mygpstracker.model.GpsInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CRestApi {
    @Headers("Content-Type: application/json")
    @POST("coordinate/")
    fun addGps(@Body gpsInfo: GpsInfo): Call<GpsInfo>
}