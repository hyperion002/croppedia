package com.example.croppedia.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.croppedia.data.Repository
import com.example.croppedia.model.ApiResponse
import com.example.croppedia.model.Crop
import com.example.croppedia.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _readCrops: LiveData<List<Crop>> = repository.local.getAllCrops().asLiveData()
    val readCrops: LiveData<List<Crop>> = _readCrops

    private fun insertCrops(crops: List<Crop>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertCrops(crops)
        }
    }

    private val _getAllCropsResponse: MutableLiveData<NetworkResult<ApiResponse>> = MutableLiveData()
    val getAllCropsResponse: LiveData<NetworkResult<ApiResponse>> = _getAllCropsResponse

    fun getAllCrops() = viewModelScope.launch {
        getAllCropsSafeCall()
    }

    private suspend fun getAllCropsSafeCall() {
        _getAllCropsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getAllCrops()
                _getAllCropsResponse.value = handleApiResponse(response)

                val apiResponse = _getAllCropsResponse.value!!.data
                if (apiResponse != null) {
                    offlineCacheRecipes(apiResponse.crops)
                }
            } catch (e: Exception) {
                _getAllCropsResponse.value = NetworkResult.Error(message = "Crops not found!")
            }
        } else {
            _getAllCropsResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }
    }

    private fun handleApiResponse(response: ApiResponse): NetworkResult<ApiResponse> {
        return when {
            response.message.toString().contains("timeout") -> {
                NetworkResult.Error(message = "Timeout")
            }
            response.crops.isEmpty() -> {
                NetworkResult.Error(message = "Empty list returned")
            }
            response.success -> {
                NetworkResult.Success(response)
            }
            else -> {
                NetworkResult.Error(message = response.message)
            }
        }
    }


    private fun offlineCacheRecipes(crops: List<Crop>) {
        insertCrops(crops)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}