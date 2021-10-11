package com.example.kutuki.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response

data class CategoryResponse(
    @SerializedName("code") val code: String,
    @SerializedName("response") val response: ResponseModel
)

data class ResponseModel(
    @SerializedName("videoCategories") val videoCategories: MutableList<CategoryModel>
)

data class CategoryModel(
    val name: String,
    val image:String
)

