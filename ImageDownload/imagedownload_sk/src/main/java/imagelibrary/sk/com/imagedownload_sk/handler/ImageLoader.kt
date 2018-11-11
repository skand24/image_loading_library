package imagelibrary.sk.com.imagedownload_sk.handler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import imagelibrary.sk.com.imagedownload_sk.R
import imagelibrary.sk.com.imagedownload_sk.utils.BitmapCache
import imagelibrary.sk.com.imagedownload_sk.utils.DiskBitmapCache
import imagelibrary.sk.com.imagedownload_sk.utils.MemoryBitmapCache
import imagelibrary.sk.com.imagedownload_sk.utils.NetworkClient
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class ImageLoader private constructor(private val mMemoryCache: MemoryBitmapCache, private val mDiskCache: DiskBitmapCache,
                                      private val mNetworkClient: NetworkClient) {


    private val compositeDisposable = CompositeDisposable()

    fun imgDownload(viw: ImageView, url: String) {
        loadFromCache(url, mMemoryCache)?.let {
            result(it, viw)
        } ?: run {
            val diskImageObservable = loadFromCache(url, mDiskCache)
            diskImageObservable?.doOnNext(saveToCache(url, mMemoryCache))?.let {
                result(it, viw)
            } ?: run {
                val dispose = mNetworkClient
                        .loadImage(url)
                        .doOnNext(saveToCache(url, mMemoryCache))
                        .doOnNext(saveToCache(url, mDiskCache))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<Bitmap>() {
                            override fun onComplete() {
                                Log.d(TAG, "Sucess")
                            }

                            override fun onNext(t: Bitmap) {
                                onsucessView(viw, t)
                            }

                            override fun onError(e: Throwable) {
                                Log.d(TAG, e.message)
                                notfound(viw)
                            }

                        })
                compositeDisposable.add(dispose)

            }
        }
    }

    private fun result(imageBitmap: Observable<Bitmap>, view: ImageView) {
        val dispose = imageBitmap.subscribeWith(object : DisposableObserver<Bitmap>() {
            override fun onComplete() {
                Log.d(TAG, "Sucess")
            }

            override fun onNext(t: Bitmap) {
                onsucessView(view, t)
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, e.message)
            }

        })
        compositeDisposable.add(dispose)
    }

    private fun notfound(view: ImageView) {
        val bmp = BitmapFactory.decodeResource(mDiskCache.cont.resources, R.drawable.nf)
        onsucessView(view, bmp)
    }

    private fun loadProgressView(viw: ImageView) {
        val bmp = BitmapFactory.decodeResource(mDiskCache.cont.resources, R.drawable.progress)
        onsucessView(viw, bmp)
    }

    private fun onsucessView(view: ImageView, bitmap: Bitmap) {
        Handler().post {
            view.setImageBitmap(bitmap)
        }

    }

    private fun saveToCache(imageUrl: String, bitmapCache: BitmapCache): Consumer<Bitmap> {
        return Consumer { bitmap ->
            if (bitmap == null) {
                Log.d(TAG, "Can't save empty data")
            }
            Log.i(TAG, "Saving to: " + bitmapCache.name)
            bitmapCache.save(imageUrl, bitmap!!)
        }
    }

    private fun loadFromCache(imageUrl: String, whichBitmapCache: BitmapCache): Observable<Bitmap>? {
        val imageBitmap = whichBitmapCache[imageUrl]
        if (imageBitmap != null) {
            return Observable
                    .just(imageBitmap)
                    .compose(logCacheSource(whichBitmapCache))
        } else {
            return null
        }
    }

    private fun logCacheSource(whichBitmapCache: BitmapCache): ObservableTransformer<Bitmap, Bitmap>? {
        return ObservableTransformer {
            it.doOnNext { bimp ->
                val cacheName = whichBitmapCache.name
                Log.i(TAG, "Checking: $cacheName")
                if (bimp == null) {
                    Log.i(TAG, "Does not have this Url")
                } else {
                    Log.i(TAG, "Url found in cache!")
                }
            }
        }
    }

    fun clearCache() {
        compositeDisposable.dispose()
        mDiskCache.clear()
        mMemoryCache.clear()
    }

    companion object {

        private val TAG = "Photos"
        private val LOCK = Any()
        @Volatile
        private var sImageLoader: ImageLoader? = null

        fun getInstance(context: Context): ImageLoader {
            if (sImageLoader == null) {
                synchronized(LOCK) {
                    if (sImageLoader == null) {
                        sImageLoader = ImageLoader(
                                MemoryBitmapCache.getInstance(),
                                DiskBitmapCache.getInstance(context),
                                NetworkClient.getInstance()
                        )
                    }
                }
            }
            return sImageLoader ?: getInstance(context)
        }
    }

}