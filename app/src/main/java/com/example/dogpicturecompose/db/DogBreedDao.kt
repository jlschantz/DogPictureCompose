package com.example.dogpicturecompose.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DogBreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDogBreeds(dogBreeds: List<DogBreed>)

    @Query("DELETE FROM dog_breeds")
    suspend fun deleteDogBreeds()

    @Query("SELECT * FROM dog_breeds WHERE name LIKE :letters || '%'")
    fun getDogBreedsStartingWith(letters: String): Flow<List<DogBreed>>
}