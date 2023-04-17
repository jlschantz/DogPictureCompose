package com.example.dogpicturecompose.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.composelayouts.PictureRow
import com.example.dogpicturecompose.composelayouts.SearchAppBar
import com.example.dogpicturecompose.composelayouts.TopAppBar
import com.example.dogpicturecompose.ui.theme.DogPictureComposeTheme
import com.example.dogpicturecompose.ui.theme.Purple_500
import com.example.dogpicturecompose.viewmodels.DogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dogViewModel: DogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogPictureComposeTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    var searchBarDisplayed by remember {
                        mutableStateOf(false)
                    }

                    if(searchBarDisplayed){
                        SearchAppBar(
                            onSearchButtonClicked = { query ->
                                dogViewModel.searchForType(query)
                            },
                            onCloseIconClicked = {
                                searchBarDisplayed = false
                            }
                        )
                    } else {
                        TopAppBar(onSearchIconClicked = {
                                searchBarDisplayed = true
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (dogViewModel.searchForTypeResult is ResultState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = Purple_500
                            )
                        } else {
                            val dogPictureList: List<*>? = dogViewModel.searchForTypeResult.data as? List<*>

                            if (dogPictureList.isNullOrEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.no_results),
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            } else {
                                LazyColumn {
                                    items(dogPictureList) { item ->
                                        PictureRow(url = item as String)
                                    }
                                }
                            }

                            if (dogViewModel.searchForTypeResult is ResultState.Error) {
                                val context = LocalContext.current

                                Toast.makeText(
                                    context,
                                    dogViewModel.searchForTypeResult.message ?: context.getString(R.string.an_error_has_occurred),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}