package imagelibrary.sk.com.imagedownload.di.network

import android.content.Context
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteDataManager
@Inject
constructor(val context: Context) : RemoteDataSource {

    override fun getAllJsonData(jsonUrl: String): Observable<Response<JSONArray>> {
        return iNetworkAPI.getAllJsonData(jsonUrl)
    }

    private lateinit var iNetworkAPI: INetworkAPI
    private lateinit var retrofit: Retrofit

    init {
        initilizeRetroInstance()
    }

    private fun initilizeRetroInstance() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        httpClient.connectTimeout(80, TimeUnit.SECONDS)
        retrofit = Retrofit.Builder()
                .baseUrl("http://pastebin.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
        iNetworkAPI = retrofit.create(INetworkAPI::class.java)
    }
}