package com.example.dogpicturecompose.repositories

import com.example.dogpicturecompose.api.DogApi
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.db.DogBreed
import com.example.dogpicturecompose.db.DogDatabase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val dogApi: DogApi,
    private val dogDatabase: DogDatabase
) {

    private val dogBreedDao = dogDatabase.dogBreedDao()

    fun getDogPicturesByType(type: String) = flow {
        emit(ResultState.Loading())
        try {
            val response = dogApi.getDogPicturesByType(type)
            if (response.isSuccessful) {
                emit(ResultState.Success(response.body()?.message))
            }else {
                emit(ResultState.Error())
            }
        } catch (exception: Exception) {
            emit(ResultState.Error(exception.localizedMessage))
        }
    }

    fun getDogBreedList() = flow {
        emit(ResultState.Loading())
        try {
            val response = dogApi.getDogBreedList()
            if (response.isSuccessful) {
                val dogBreedList = response.body()?.message?.map { it -> DogBreed(it) }

                dogBreedList?.let {
                    dogBreedDao.insertDogBreeds(dogBreedList)
                }

                emit(ResultState.Success(response.body()?.message))
            }else {
                emit(ResultState.Error())
            }
        } catch (exception: Exception) {
            emit(ResultState.Error(exception.localizedMessage))
        }
    }

    fun getDogBreedsStartingWith(letters: String) = dogBreedDao.getDogBreedsStartingWith(letters)

}