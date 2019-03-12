package com.example

import com.example.pojo.SearchResults
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
  @GET("services/rest/")
  fun getSearchResults(@Query("page") page: Int,
                       @Query("text") text: String,
                       @Query("api_key") key: String = BuildConfig.API_KEY,
                       @Query("format") format: String = "json",
                       @Query("per_page") perPage: Int = 26,
                       @Query("nojsoncallback") cb: Int = 1,
                       @Query("method") method: String = "flickr.photos.search"): Deferred<SearchResults>
}