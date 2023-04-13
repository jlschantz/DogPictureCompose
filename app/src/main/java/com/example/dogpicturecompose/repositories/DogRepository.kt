package com.example.dogpicturecompose.repositories

import com.example.dogpicturecompose.api.DogApi
import com.example.dogpicturecompose.api.ResultState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DogRepository @Inject constructor(private val dogApi: DogApi) {

    fun getDogPicturesByType(type: String) = flow {
        emit(ResultState.Loading())
        try {
            val response = dogApi.getDogPicturesByType(type)
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()?.message))
            }else {
                emit(ResultState.Error(null))
            }
        } catch (exception: Exception) {
            emit(ResultState.Error(exception.localizedMessage))
        }
    }
}