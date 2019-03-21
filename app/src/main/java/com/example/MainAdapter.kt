package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pojo.Photo
import kotlinx.android.synthetic.main.item.view.*

class MainAdapter : RecyclerView.Adapter<ViewHolder>() {

  private val items = mutableListOf<Photo>()
  var listener: RecyclerViewClickListener? = null

  override fun getItemCount() = items.size

  fun clear() = items.clear()
  fun isEmpty() = items.isEmpty()

  fun addAll(list: List<Photo>, clear: Boolean, listView: RecyclerView) {
    if (clear) {
      items.clear()
      items.addAll(list)
      notifyDataSetChanged()
      return
    }
    val initialSize = items.size
    items.addAll(list)
    val updatedSize = items.size
    listView.post {
      notifyItemRangeInserted(initialSize,updatedSize)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false), listener)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.title.text = items.get(position).title
    GlideApp.with(holder.context).load(getUrl(position)).into(holder.image)
  }

  fun getUrl(index: Int): String {
    val pic = items.get(index)
    return "https://farm${pic.farm}.staticflickr.com/${pic.server}/${pic.id}_${pic.secret}_m.jpg"
  }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
  val title = view.text_item
  val image = view.image_item
  val context: Context = view.context

  private var listener: RecyclerViewClickListener? = null

  constructor(v: View, l: RecyclerViewClickListener?): this(v) {
    listener = l
    v.setOnClickListener(this)
  }

  override fun onClick(view: View?) {
    if (view != null) {
      listener?.onClick(view, adapterPosition)
    }
  }
}

interface RecyclerViewClickListener {
  fun onClick(view: View, position: Int)
}
