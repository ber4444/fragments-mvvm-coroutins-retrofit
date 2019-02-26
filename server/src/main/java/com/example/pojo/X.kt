package com.example.pojo

data class X(
        val dt_txt: String, // Time of data forecasted, UTC
        val main: Main,
        val weather: List<Weather> // no idea why this is a list, it always has only one item
)