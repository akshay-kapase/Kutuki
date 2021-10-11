package com.example.kutuki.data.remote

import android.util.Log
import com.google.gson.JsonObject
import org.json.JSONObject
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val categoryService: CategoryService, private val videoService: VideoService) {

    suspend fun getCategories() = categoryService.getCategories()

    suspend fun getVideos() = videoService.getVideos()

}