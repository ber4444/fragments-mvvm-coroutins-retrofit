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
      val result = Server(true).getItemsAsync(page, query).await()
      liveData.postValue(result)
    } catch (e: Exception) {
      throw e
    } catch (e: Throwable) {
      throw e
    }
  }
}
