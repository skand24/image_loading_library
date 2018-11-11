package imagelibrary.sk.com.imagedownload.mvvm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import imagelibrary.sk.com.imagedownload.di.network.RemoteDataManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import javax.inject.Inject


class MainViewmodel
@Inject
constructor
(var remoteDataManager: RemoteDataManager) : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    val jsonLiveData = MutableLiveData<ArrayList<String>>()

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    private val mOkHttpClient: OkHttpClient = OkHttpClient()


    fun jsonData() {
        val responses = Observable.fromCallable {
            val loadRequest = Request.Builder()
                    .url("http://pastebin.com/raw/wgkJgazE")
                    .build()

            val response = mOkHttpClient
                    .newCall(loadRequest)
                    .execute()
            if (response.isSuccessful) {
                var fullString = ""
                val inputStr = response.body()?.bytes()
                fullString = inputStr?.toString(Charset.defaultCharset())!!
                val charStr = response.body()?.charStream()
                var line: String

                return@fromCallable fullString
            }
            return@fromCallable null
        }
        val dispose = responses.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<String>() {
                    override fun onComplete() {
                        Log.d("a", "Sucess")
                    }

                    override fun onNext(t: String) {
                        val jsonArray = JSONArray(t)
                        //(((jsonArray.get(0) as JSONObject).get("user") as JSONObject).get("profile_image") as JSONObject).get("small")
                        doParse(jsonArray)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("b", e.message)
                    }

                })
        compositeDisposable.add(dispose)
    }

    private fun doParse(jsonArray: JSONArray) {
        val imageList = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            val jObjPrfyl = (((jsonArray.get(i) as JSONObject).get("user") as JSONObject).get("profile_image") as JSONObject)
            val jImgs = (jObjPrfyl.get("large") as String)
            imageList.add(jImgs)
        }
        jsonLiveData.value = imageList
    }


}