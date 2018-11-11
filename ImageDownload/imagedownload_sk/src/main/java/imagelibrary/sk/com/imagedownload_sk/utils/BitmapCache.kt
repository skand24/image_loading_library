package imagelibrary.sk.com.imagedownload_sk.utils

import android.graphics.Bitmap

interface BitmapCache {

    val name: String

    fun containsKey(key: String): Boolean

    operator fun get(key: String): Bitmap?

    fun save(key: String, bitmapToSave: Bitmap)

    fun clear()

}