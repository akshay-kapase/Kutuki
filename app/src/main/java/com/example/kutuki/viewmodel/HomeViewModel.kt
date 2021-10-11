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
import com.example.kutuki.utils.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<CategoryResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<CategoryResponse>> = _response

    fun fetchCategories() = viewModelScope.launch {
        repository.getCategories().collect { values ->
            _response.value = values
        }
    }

}