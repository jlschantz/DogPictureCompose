package com.example.dogpicturecompose.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.api.ResultState.*
import com.example.dogpicturecompose.repositories.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val dogRepository: DogRepository): ViewModel() {

    var dogPictureList by mutableStateOf(listOf<String>())
    var loadingState by mutableStateOf(SUCCESS)
    var errorMessage by mutableStateOf("")

    private fun getDogPicturesByType(type: String) = flow {
        emit(Loading(data = null))
        try {
            val response = dogRepository.getDogPicturesByType(type)
            if (response.isSuccessful) {
                emit(Success(data = response.body()?.message))
            }else {
                emit(Error(data = null, error = null))
            }
        } catch (exception: Exception) {
            emit(Error(data = null, error = exception.localizedMessage))
        }
    }

    fun searchForType(context: Context, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getDogPicturesByType(type).collect { resource ->
                withContext(Dispatchers.Main) {
                    when (resource) {
                        is Success -> {
                            resource.data?.let { list ->
                                loadingState = SUCCESS
                                dogPictureList = list
                            } ?: {
                                loadingState = ERROR
                                errorMessage = context.getString(R.string.an_error_has_occurred)
                            }
                        }
                        is Error -> {
                            loadingState = ERROR
                            errorMessage = resource.error.toString()
                        }
                        is Loading -> {
                            loadingState = LOADING
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val SUCCESS = "Success"
        const val ERROR = "Error"
        const val LOADING = "Loading"
    }
}