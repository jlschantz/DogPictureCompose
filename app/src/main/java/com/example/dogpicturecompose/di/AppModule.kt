package com.example.dogpicturecompose.di

import android.app.Application
import androidx.room.Room
import com.example.dogpicturecompose.api.DogApi
import com.example.dogpicturecompose.db.DogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
        .baseUrl(DogApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit): DogApi =
        retrofit.create(DogApi::class.java)

    @Provides
    @Singleton
    fun provideDogDatabase(app: Application): DogDatabase {
        return Room.databaseBuilder(app, DogDatabase::class.java, "dog_breeds_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}