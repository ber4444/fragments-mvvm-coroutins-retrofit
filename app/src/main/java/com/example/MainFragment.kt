package com.example

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_landing.*

class MainFragment : Fragment() {
  private val adapter = MainAdapter()
  private val viewModel: MainViewModel by lazy {
    ViewModelProviders.of(this).get(MainViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?) =
    inflater.inflate(R.layout.fragment_landing, container, false)!!

  fun search(query: String) {
    adapter.clear()
    getItems(1, query, true)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    swipe_refresh_main.setOnRefreshListener { getItems(1, (activity as MainActivity).query, true) }

    recycler_main.setHasFixedSize(true)
    val gridSpans = 2
    val layoutManager = StaggeredGridLayoutManager(gridSpans, StaggeredGridLayoutManager.VERTICAL)
    recycler_main.layoutManager = layoutManager
    recycler_main.adapter = adapter

    recycler_main.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
      override fun onLoadMore(page: Int, totalItemsCount: Int) {
        val max = (activity as MainActivity).maxPages
        if (page+1 > max) return
        getItems(page+1, (activity as MainActivity).query, false)
      }
    })

    adapter.listener = object : RecyclerViewClickListener {
      override fun onClick(view: View, position: Int) {
        val intent = Intent(view.context, SecondActivity::class.java)
        intent.putExtra("URL", adapter.getUrl(position))
        startActivity(intent)
      }
    }

    if (adapter.isEmpty()) getItems(1, (activity as MainActivity).query, true)
  }

  fun getItems(page: Int, query: String?, clear: Boolean) {
    swipe_refresh_main.isRefreshing = false
    if (query.isNullOrBlank()) return
    viewModel.readonly().observe(viewLifecycleOwner, Observer { // don't pass 'this' here to avoid https://github.com/googlesamples/android-architecture-components/issues/47
      if (it!!.stat == "fail") Toast.makeText(activity!!, it.message, Toast.LENGTH_SHORT).show()
      else {
        if (it.photos == null) adapter.addAll(emptyList(), clear, recycler_main)
        else {
          val list = it.photos!!.photo
          (activity as MainActivity).maxPages = it.photos!!.pages
          adapter.addAll(list, clear, recycler_main)
        }
      }
    })
    viewModel.getItems(page, query)
  }
}