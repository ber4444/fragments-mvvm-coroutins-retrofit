package com.example.pojo

data class SearchResults(
    val photos: Photos?,
    val stat: String, // "ok" or "fail" (in the latter case, the error will be in message)
    val message: String
)
