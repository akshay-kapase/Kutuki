package com.example.kutuki.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kutuki.data.Repository
import com.example.kutuki.model.CategoryModel
import com.example.kutuki.model.CategoryResponse
import com.example.kutuki.model.ThumbnailResponse
import com.example.kutuki.utils.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<ThumbnailResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<ThumbnailResponse>> = _response

    fun fetchVideos() = viewModelScope.launch {
        repository.getVideos().collect { values ->
            _response.value = values
        }
    }

}