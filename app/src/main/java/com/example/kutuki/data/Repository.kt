package com.example.kutuki.data

import android.util.Log
import com.example.kutuki.data.remote.RemoteDataSource
import com.example.kutuki.model.*
import com.example.kutuki.utils.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getCategories(): Flow<NetworkResult<CategoryResponse>> {
        return flow<NetworkResult<CategoryResponse>> {
            emit(safeApiCall { remoteDataSource.getCategories() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVideos(): Flow<NetworkResult<ThumbnailResponse>> {
        return flow<NetworkResult<ThumbnailResponse>> {
            emit(safeApiCall { remoteDataSource.getVideos() })
        }.flowOn(Dispatchers.IO)
    }

}
