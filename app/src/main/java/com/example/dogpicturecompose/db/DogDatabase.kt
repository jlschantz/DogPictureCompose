package com.example.dogpicturecompose.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogBreed::class],
    version = 1
)
abstract class DogDatabase: RoomDatabase() {

    abstract fun dogBreedDao(): DogBreedDao
}