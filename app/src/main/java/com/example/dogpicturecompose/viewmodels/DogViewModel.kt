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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val dogRepository: DogRepository): ViewModel() {

    var resultState: ResultState<*> by mutableStateOf(Success<List<String>>(listOf()))

    fun searchForType(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dogRepository.getDogPicturesByType(type).collect { resource ->
                withContext(Dispatchers.Main) {
                    when (resource) {
                        is Success -> {
                            resource.data?.let { list ->
                                resultState = Success(list)
                            } ?: {
                                resultState = Error<List<String>>(null)
                            }
                        }
                        is Error -> {
                            resultState = Error<List<String>>(resource.message.toString())
                        }
                        is Loading -> {
                            resultState = Loading<List<String>>()
                        }
                    }
                }
            }
        }
    }
}