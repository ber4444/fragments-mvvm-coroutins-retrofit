package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pojo.X
import kotlinx.android.synthetic.main.item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter : RecyclerView.Adapter<ViewHolder>() {

  private val items = mutableListOf<X>()
  var listener: RecyclerViewClickListener? = null

  override fun getItemCount() = items.size

  fun clear() = items.clear()
  fun isEmpty() = items.isEmpty()

  fun addAll(list: List<X>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false), listener)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val celsius = items.get(position).main.temp
    val descr = items.get(position).weather[0].description
    holder.title.text = "$celsius Celsius, $descr"
    holder.time.text = items.get(position).dt_txt.toDate().formatTo("MM-dd-yyyy HH:mm")
    val image = items.get(position).weather[0].icon
    GlideApp.with(holder.context).load("http://openweathermap.org/img/w/$image.png").into(holder.image)
  }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
  val title = view.title
  val time = view.time
  val image = view.image
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

// utils:

fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
  val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
  parser.timeZone = timeZone
  return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
  val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
  formatter.timeZone = timeZone
  return formatter.format(this)
}