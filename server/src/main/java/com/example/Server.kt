package com.example

import com.example.pojo.SearchResults
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockWebServer

class Server {
  private val apiService : ApiService
  private val mockWebServer = MockWebServer()

  constructor (useMock: Boolean = false) {
    if (useMock) {
      mockWebServer.start()
      enqueueResponse("http/SampleResult.json")
      apiService = buildRetrofit(mockWebServer.url("").url().toString()).create(ApiService::class.java)
    } else {
      apiService = buildRetrofit("https://api.openweathermap.org/data/2.5/").create(ApiService::class.java)
    }
  }

  private fun enqueueResponse(file: String, status: Int = 200) {
    val resource = this::class.java.classLoader!!.getResource(file)
    mockWebServer.enqueue(MockResponse().setResponseCode(status).setBody(resource.readText()))
  }

  fun getItemsAsync(page: Int, query: String) : Deferred<SearchResults> {
    return apiService.getSearchResults(page, query)
  }

  private fun getOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
      val interceptor = HttpLoggingInterceptor()
      interceptor.level = HttpLoggingInterceptor.Level.BODY

      builder.addInterceptor(interceptor)
    }

    return builder.build()
  }

  internal fun buildRetrofit(baseUrl: String): Retrofit {
    val gson = GsonBuilder()
        .create()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
  }
}