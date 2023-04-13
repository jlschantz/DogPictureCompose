package com.example.dogpicturecompose.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogpicturecompose.api.ResultState
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

    var resultState: ResultState<*> by mutableStateOf(Success<List<String>>(listOf()))

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

    fun searchForType(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getDogPicturesByType(type).collect { resource ->
                withContext(Dispatchers.Main) {
                    when (resource) {
                        is Success -> {
                            resource.data?.let { list ->
                                resultState = Success(list)
                            } ?: {
                                resultState = Error<List<String>>(error = null)
                            }
                        }
                        is Error -> {
                            resultState = Error<List<String>>(error = resource.error.toString())
                        }
                        is Loading -> {
                            resultState = Loading<List<String>>(listOf())
                        }
                    }
                }
            }
        }
    }
}