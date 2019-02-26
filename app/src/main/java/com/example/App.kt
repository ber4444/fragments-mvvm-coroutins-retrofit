package com.example

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class App : AppGlideModule() {
  override fun applyOptions(context: Context, builder: GlideBuilder) {
    builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context))
  }
}