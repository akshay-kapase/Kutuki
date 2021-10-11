package com.example.kutuki.model

import com.google.gson.annotations.SerializedName

data class ThumbnailResponse(
    @SerializedName("code") val code: String,
    @SerializedName("response") val response: VideosModelResponse
)

data class VideosModelResponse(
    @SerializedName("videos") val videos: MutableList<VideosModel>
)

data class VideosModel(
    val title: String,
    val description:String,
    val thumbnailURL:String,
    val videoURL:String,
    val categories:String
)

