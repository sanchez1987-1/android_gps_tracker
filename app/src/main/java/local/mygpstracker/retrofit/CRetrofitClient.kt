package local.mygpstracker.retrofit

import local.mygpstracker.Interface.CRestApi
import local.mygpstracker.model.GpsInfo
import okhttp3.Credentials
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//https://habr.com/ru/post/520544/
object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private var credentials: String = Credentials.basic(username, password)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}

object ServiceBuilder {
    private val BASE_URL = "https://devsrv.tomymail.cloudns.cl/"
    private val client = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor("admin",     "123qweASD"))
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

class RestApiService {
    fun addGps(gpsInfo: GpsInfo, onResult: (GpsInfo?) -> Unit){
        val retrofit = ServiceBuilder.buildService(CRestApi::class.java)
        retrofit.addGps(gpsInfo).enqueue(
            object : Callback<GpsInfo> {
                override fun onFailure(call: Call<GpsInfo>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<GpsInfo>, response: Response<GpsInfo>) {
                    val addedGps = response.body()
                    onResult(addedGps)
                }
            }
        )
    }
}