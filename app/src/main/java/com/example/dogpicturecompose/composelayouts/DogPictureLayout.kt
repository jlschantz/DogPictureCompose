package com.example.dogpicturecompose.composelayouts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.api.ResultState
import com.example.dogpicturecompose.ui.theme.Purple_500
import com.example.dogpicturecompose.viewmodels.DogViewModel

@Composable
fun DogPictureLayout(dogViewModel: DogViewModel) {
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
            if (dogViewModel.resultState is ResultState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = Purple_500
                )
            } else {
                val dogPictureList: List<*>? = dogViewModel.resultState.data as? List<*>

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

                if (dogViewModel.resultState is ResultState.Error) {
                    val context = LocalContext.current

                    Toast.makeText(
                        context,
                        dogViewModel.resultState.message ?: context.getString(R.string.an_error_has_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
fun TopAppBar(onSearchIconClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple_500)
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.dog_pictures),
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(PaddingValues(10.dp,0.dp,0.dp,0.dp))

        )
        IconButton(onClick = onSearchIconClicked) {
            Icon(
                modifier = Modifier.size(26.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search),
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onCloseIconClicked: () -> Unit,
    onSearchButtonClicked: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple_500)
            .padding(3.dp),
        value = text,
        onValueChange = { newText -> text = newText },
        leadingIcon = {
            IconButton(onClick = onCloseIconClicked) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = Color.White
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.White,
            containerColor = Purple_500,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchButtonClicked(text) }
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_by_dog_breed),
                color = Color.LightGray
            )
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureRow(url: String) {
    Box(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        GlideImage(
            model = url,
            contentDescription = stringResource(id = R.string.dog_picture),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}