package com.example.dogpicturecompose.composelayouts

import android.util.Log
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.ui.theme.Purple_500
import com.example.dogpicturecompose.viewmodels.DogViewModel

@Composable
fun DogPictureListLayout(dogViewModel: DogViewModel) {

    ConstraintLayout(
        constraints(),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        var searchBarDisplayed by remember {
            mutableStateOf(false)
        }

        var showDropDown by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple_500)
                .defaultMinSize(0.dp,60.dp)
                .layoutId("topAppBar"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (searchBarDisplayed) {
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
                        if (showDropDown) {
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
        }

        if (dogViewModel.searchForTypeResult is ResultState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .layoutId("circularProgressIndicator"),
                color = Purple_500
            )
        } else {
            val dogPictureList: List<*>? = dogViewModel.searchForTypeResult.data as? List<*>

            if (dogPictureList.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.layoutId("noResultsText"),
                    text = stringResource(id = R.string.no_results),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.layoutId("dogPictureList")
                ) {
                    items(dogPictureList) { item ->
                        PictureRow(url = item as String)
                    }
                }
            }

            if (dogViewModel.searchForTypeResult is ResultState.Error && !showDropDown) {
                val context = LocalContext.current

                Toast.makeText(
                    context,
                    if(dogViewModel.searchForTypeResult.message.isNullOrEmpty())
                        context.getString(R.string.an_error_has_occurred)
                    else
                        dogViewModel.searchForTypeResult.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val focusManager = LocalFocusManager.current

        if (showDropDown) {
            LazyColumn(
                modifier = Modifier.layoutId("filteredDogBreedList")
            ) {
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

private fun constraints(): ConstraintSet {
    return ConstraintSet {

        val topAppBar = createRefFor("topAppBar")
        val circularProgressIndicator = createRefFor("circularProgressIndicator")
        val noResultsText = createRefFor("noResultsText")
        val dogPictureList = createRefFor("dogPictureList")
        val filteredDogBreedList = createRefFor("filteredDogBreedList")

        constrain(topAppBar){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(circularProgressIndicator){
            top.linkTo(topAppBar.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(noResultsText){
            top.linkTo(topAppBar.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(dogPictureList){
            top.linkTo(topAppBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(filteredDogBreedList){
            top.linkTo(topAppBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}