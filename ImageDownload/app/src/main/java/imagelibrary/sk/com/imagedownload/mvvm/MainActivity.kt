package imagelibrary.sk.com.imagedownload.mvvm

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import imagelibrary.sk.com.imagedownload.R
import imagelibrary.sk.com.imagedownload.base.BaseActivity
import imagelibrary.sk.com.imagedownload_sk.handler.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewmodel>(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var mainViewmodel: MainViewmodel

    override fun getViewModel(): MainViewmodel = mainViewmodel

    override fun onRefresh() {
        swipe_container.setRefreshing(true)
        mainViewmodel.jsonData()
    }

    var imageloader: ImageLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        imageloader = ImageLoader.getInstance(this@MainActivity)
        my_recycler_view.layoutManager = GridLayoutManager(this@MainActivity, 2, LinearLayoutManager.VERTICAL, false)
        my_recycler_view.setHasFixedSize(true)
        my_recycler_view.itemAnimator = DefaultItemAnimator()
        swipe_container.setOnRefreshListener(this);
        swipe_container.post {
            swipe_container.setRefreshing(true)
            mainViewmodel.jsonData()
        }
        mainViewmodel.jsonLiveData.observe(this, Observer {
            swipe_container.setRefreshing(false)
            it?.let { arr ->
                arrayList = ArrayList()
                if (arr.isNotEmpty()) {
                    uniqueImg.addAll(arr)
                    if (arrayList.isNotEmpty()) {
                        arrayList.clear()
                    }
                    arrayList.addAll(uniqueImg)
                    createRecyclerview()
                }
            }
        })
    }

    private val uniqueImg = HashSet<String>()
    private lateinit var arrayList: ArrayList<String>
    private lateinit var adapter: MainAdapter
    private fun createRecyclerview() {
        adapter = MainAdapter(arrayList) { imgView, str -> loadImage(imageView = imgView, imgUrl = str) }
        my_recycler_view.adapter = adapter
    }

    private fun loadImage(imageView: ImageView, imgUrl: String) {
        imageloader?.imgDownload(imageView, imgUrl)
    }

}
