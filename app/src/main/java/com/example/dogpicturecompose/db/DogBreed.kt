package com.example.dogpicturecompose.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_breeds")
data class DogBreed (
    @PrimaryKey val name: String
)