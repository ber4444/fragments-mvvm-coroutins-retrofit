package com.example

import com.example.pojo.SearchResults
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testServer() {
    var res : Deferred<SearchResults>
    runBlocking {
      res = Server(true).getItemsAsync(1, "524901")
      val list = res.await().list
      assertNotNull(list)
      assertEquals(list!!.size, 40)
    }
  }
}