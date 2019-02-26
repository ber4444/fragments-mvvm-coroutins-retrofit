package com.example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pojo.SearchResults

class MainViewModel : ViewModel() {

  private val itemsLiveData = MutableLiveData<SearchResults>()
  fun readonly(): LiveData<SearchResults> = itemsLiveData
  private var query: String? = null

  fun getItems(page: Int, query: String) {
    if (itemsLiveData.value==null || !this.query.equals(query))
      Repo.getItems(itemsLiveData, page, query)
    this.query = query
  }
}