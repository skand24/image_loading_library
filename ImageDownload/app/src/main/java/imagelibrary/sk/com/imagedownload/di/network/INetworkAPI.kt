package imagelibrary.sk.com.imagedownload.di.network

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface INetworkAPI {

    @GET
    fun getAllJsonData(@Url jsonUrl: String): Observable<Response<JSONArray>>
}