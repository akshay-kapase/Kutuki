package com.example.kutuki.data.remote

import com.example.kutuki.model.CategoryResponse
import com.example.kutuki.model.ThumbnailResponse
import com.example.kutuki.utils.Constants
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {

    @GET(Constants.THUMBNAIL_URL)
    suspend fun getVideos(): Response<ThumbnailResponse>

}
