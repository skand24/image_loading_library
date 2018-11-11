package imagelibrary.sk.com.imagedownload_sk.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkClient {
    private val mOkHttpClient: OkHttpClient = OkHttpClient()

    fun loadImage(imageUrl: String): Observable<Bitmap> {
        return Observable.fromCallable {
            val loadRequest = Request.Builder()
                    .url(imageUrl)
                    .build()

            val response = mOkHttpClient
                    .newCall(loadRequest)
                    .execute()
            if (response.isSuccessful) {
                return@fromCallable BitmapFactory.decodeStream(response.body()!!.byteStream())
            }
            return@fromCallable null
        }
    }

    companion object {

        @JvmStatic
        fun getInstance() = NetworkClient()
    }

}