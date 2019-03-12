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
      res = Server(true).getItemsAsync(1, "blah")
      val result = res.await().photos
      assertNotNull(result)
      assertEquals(result!!.photo.size, result.perpage)
    }
  }
}