package com.example

import com.example.pojo.SearchResults
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  @GET("forecast")
  fun getSearchResults(@Query("page") page: Int,
                       @Query("q") text: String,
                       @Query("appid") key: String = BuildConfig.API_KEY,
                       @Query("lang") lang: String = "hu",
                       @Query("units") units: String = "metric"): Deferred<SearchResults>
}