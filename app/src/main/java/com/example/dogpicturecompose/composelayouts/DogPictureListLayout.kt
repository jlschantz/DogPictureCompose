package com.example.dogpicturecompose.composelayouts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.ui.theme.Purple_500
import com.example.dogpicturecompose.viewmodels.DogViewModel

@Composable
fun DogPictureListLayout(dogViewModel: DogViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var searchBarDisplayed by remember {
            mutableStateOf(false)
        }

        var showDropDown by remember {
            mutableStateOf(false)
        }

        if(searchBarDisplayed){
            SearchAppBar(
                onSearchButtonClicked = { query ->
                    showDropDown = false
                    dogViewModel.searchForType(query)
                },
                onCloseIconClicked = {
                    searchBarDisplayed = false
                },
                onValueChange = { query ->
                    showDropDown = query.isNotEmpty()
                    if(showDropDown){
                        dogViewModel.getDogBreedsStartingWith(query)
                    }
                }
            )
        } else {
            TopAppBar(
                onSearchIconClicked = {
                    searchBarDisplayed = true
                }
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ) {
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
                            dogViewModel.searchForTypeResult.message
                                ?: context.getString(R.string.an_error_has_occurred),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val focusManager = LocalFocusManager.current

            if(showDropDown) {
                LazyColumn {
                    items(dogViewModel.filteredDogBreedList) { item ->
                        Divider(color = Color.White)
                        Text(
                            text = item.name,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    focusManager.clearFocus()
                                    showDropDown = false
                                    dogViewModel.searchForType(item.name)
                                }
                                .background(Purple_500)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}