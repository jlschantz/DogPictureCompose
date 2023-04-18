package com.example.dogpicturecompose.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.api.ResultState.*
import com.example.dogpicturecompose.db.DogBreed
import com.example.dogpicturecompose.repositories.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val dogRepository: DogRepository): ViewModel() {

    var searchForTypeResult: ResultState<*> by mutableStateOf(Success<List<String>>(listOf()))
    var filteredDogBreedList: List<DogBreed> by mutableStateOf(listOf())

    init {
        getDogBreedList()
    }

    fun searchForType(type: String) {
        viewModelScope.launch {
            dogRepository.getDogPicturesByType(type).collect { resource ->
                when (resource) {
                    is Success -> {
                        resource.data?.let { list ->
                            searchForTypeResult = Success(list)
                        } ?: {
                            searchForTypeResult = Error<List<String>>(null)
                        }
                    }
                    is Error -> {
                        searchForTypeResult = Error<List<String>>(resource.message.toString())
                    }
                    is Loading -> {
                        searchForTypeResult = Loading<List<String>>()
                    }
                }
            }
        }
    }

    private fun getDogBreedList() {
        viewModelScope.launch {
            dogRepository.getDogBreedList()
        }
    }

    fun getDogBreedsStartingWith(letters: String) {
        viewModelScope.launch {
            dogRepository.getDogBreedsStartingWith(letters).collect { resource ->
                filteredDogBreedList = resource
            }
        }
    }
}