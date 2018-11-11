package imagelibrary.sk.com.imagedownload.mvvm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import imagelibrary.sk.com.imagedownload.R
import kotlinx.android.synthetic.main.list_view.view.*

class MainAdapter(private var items: ArrayList<String>, private val loadListener: (ImageView, String) -> Unit) : RecyclerView.Adapter<MainAdapter.MainViewholder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MainViewholder {
        return MainViewholder(LayoutInflater.from(p0.context).inflate(R.layout.list_view, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun loadAgain(imageList: ArrayList<String>) {
        this.items = imageList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(p0: MainViewholder, p1: Int) {
        p0.bind(items.get(p1))
    }

    inner class MainViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imgUrl: String) {
            loadListener(itemView.displayImg, imgUrl)
        }
    }
}