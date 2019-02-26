package com.example

import androidx.lifecycle.MutableLiveData
import com.example.Server
import com.example.pojo.SearchResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repo {

  fun getItems(liveData: MutableLiveData<SearchResults>,
    page: Int, query: String) = CoroutineScope(Dispatchers.IO).launch {
    try {
      val result = Server().getItemsAsync(page, query).await()
      liveData.postValue(result)
    } catch (e: Throwable) {
      liveData.postValue(SearchResults(null, e.message)) // FIXME: UGLY
    }
  }
}
