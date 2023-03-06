package com.example.dogpicturecompose.repositories

import com.example.dogpicturecompose.api.DogApi
import com.example.dogpicturecompose.api.DogPictureResponse
import retrofit2.Response
import javax.inject.Inject

class DogRepository @Inject constructor(private val dogApi: DogApi) {

    suspend fun getDogPicturesByType(type: String): Response<DogPictureResponse>{
        return dogApi.getDogPicturesByType(type)
    }
}