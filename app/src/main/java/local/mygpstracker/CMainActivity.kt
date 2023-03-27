package local.mygpstracker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import local.mygpstracker.model.Coordinates
import local.mygpstracker.model.GpsInfo
import local.mygpstracker.retrofit.RestApiService
import java.text.SimpleDateFormat
import java.util.*

class CMainActivity: AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2
    private val database by lazy { CoordinatesDatabase.getDatabase(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cmain)
        title = "MyGPSTracker"
        val button_get: Button = findViewById(R.id.getLocation)
        button_get.setOnClickListener {
            getLocation()
        }
        val button_send: Button = findViewById(R.id.sendLocation)
        button_send.setOnClickListener {
            sendLocation(10.222,10.444)
        }
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        sendLocation(locationManager.getCurrentLocation())
    }
    override fun onLocationChanged(location: Location) {

        tvGpsLocation = findViewById(R.id.textView)
        tvGpsLocation.text = "Широта: " + location.latitude + " , Долгота: " + location.longitude
        GlobalScope.launch {
            database.CoordinatesDao().insertAll(Coordinates(id = null, event_date = getCuTime(), latitude = location.latitude.toFloat(), longitude = location.longitude.toFloat()))
        }
        sendLocation(location.latitude.toDouble(), location.longitude.toDouble())
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun sendLocation(latitude: Double, longitude: Double) {
        val apiService = RestApiService()
        val gpsInfo = GpsInfo(
            device = 1,
            latitude = latitude,
            longitude = longitude
        )

        apiService.addGps(gpsInfo) {
            if (it?.device != null) {
                // it = newly added user parsed as response
                // it?.id = newly added user ID
            } else {
                Toast.makeText(this, "Error GPS", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getCuTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val current = formatter.format(time)
        return (current)
    }
    fun closeDatabase() {
        if(database.isOpen) {
            database.openHelper.close()
        }
    }
}