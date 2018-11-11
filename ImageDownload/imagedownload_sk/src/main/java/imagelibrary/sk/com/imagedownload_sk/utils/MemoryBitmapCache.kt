package imagelibrary.sk.com.imagedownload_sk.utils

import android.graphics.Bitmap
import android.util.LruCache



class MemoryBitmapCache : BitmapCache {

    private val mCache = LruCache<String, Bitmap>(CACHE_SIZE_BYTES)

    override val name: String = MEMCACHE

    override fun containsKey(key: String): Boolean {
        synchronized(mCache) {
            val existingBitmap = get(key)
            return existingBitmap != null
        }
    }

    override fun get(key: String): Bitmap? {
        return mCache.get(key)
    }

    override fun save(key: String, bitmapToSave: Bitmap) {
        mCache.put(key, bitmapToSave)
    }

    override fun clear() {
        mCache.evictAll()
    }

    companion object {

        val CACHE_SIZE_BYTES = (Runtime.getRuntime().maxMemory() / 1024 / 2000).toInt()

        @JvmStatic
        fun getInstance() = MemoryBitmapCache()
    }

}