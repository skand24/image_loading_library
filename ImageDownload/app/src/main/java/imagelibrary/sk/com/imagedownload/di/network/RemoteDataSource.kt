package imagelibrary.sk.com.imagedownload.di.network

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONArray
import retrofit2.Response

interface RemoteDataSource {

    fun getAllJsonData(jsonUrl: String): Observable<Response<JSONArray>>
}