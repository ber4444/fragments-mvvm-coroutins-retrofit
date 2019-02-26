package com.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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
    getItems(1, query)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    swipe_refresh_main.setColorSchemeResources(R.color.colorPrimary)
    swipe_refresh_main.setOnRefreshListener { getItems(1, (activity as MainActivity).query()) }
    recycler_main.setHasFixedSize(true)

    val layoutManager = LinearLayoutManager(activity)
    recycler_main.layoutManager = layoutManager
    recycler_main.adapter = adapter


    adapter.listener = object : RecyclerViewClickListener {
      override fun onClick(view: View, position: Int) {
        //val intent = Intent(view.context, SecondActivity::class.java)
        //intent.putExtra("TITLE", adapter.getTitle(position))
        //startActivity(intent)
      }
    }

    if (adapter.isEmpty()) getItems(1, (activity as MainActivity).query())
  }

  fun getItems(page: Int, query: String?) {
    if (query.isNullOrBlank()) return
    viewModel.readonly().observe(viewLifecycleOwner, Observer { // don't pass 'this' here to avoid https://github.com/googlesamples/android-architecture-components/issues/47
      swipe_refresh_main.isRefreshing = false
      if (it!!.err != null) Toast.makeText(activity!!, it.err!!.toString(), Toast.LENGTH_SHORT).show()
      else {
        @Suppress("SENSELESS_COMPARISON")
        val list = if (it.list == null) emptyList() else it.list
        adapter.addAll(list!!)
      }
    })
    viewModel.getItems(page, query)
    swipe_refresh_main.isRefreshing = true
  }
}