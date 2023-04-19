package com.example.dogpicturecompose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.dogpicturecompose.composelayouts.DogPictureListLayout
import com.example.dogpicturecompose.ui.theme.DogPictureComposeTheme
import com.example.dogpicturecompose.viewmodels.DogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogPictureListActivity : ComponentActivity() {

    private val dogViewModel: DogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogPictureComposeTheme {
                DogPictureListLayout(dogViewModel = dogViewModel)
            }
        }
    }
}