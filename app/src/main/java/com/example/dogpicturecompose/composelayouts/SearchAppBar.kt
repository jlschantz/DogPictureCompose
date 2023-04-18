package com.example.dogpicturecompose.composelayouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.db.DogBreed
import com.example.dogpicturecompose.ui.theme.Purple_500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onCloseIconClicked: () -> Unit,
    onSearchButtonClicked: (String) -> Unit,
    onSearchDogBreeds: (String) -> Unit,
    filteredDogBreedList: List<DogBreed>
) {
    var text by remember { mutableStateOf("") }
    var showDropDown by remember { mutableStateOf(false) }
    var searched by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple_500)
                .padding(3.dp),
            value = text,
            onValueChange = { newText ->
                Log.i("tag", "newText $newText")
                text = newText
                searched = false
                onSearchDogBreeds(text)
            },
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
                unfocusedTextColor = Color.White,
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
                onSearch = {
                    searched = true
                    onSearchButtonClicked(text)
                }
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_by_dog_breed),
                    color = Color.LightGray
                )
            }
        )

        showDropDown = filteredDogBreedList.isNotEmpty() && text.isNotEmpty() && searched == false
        val focusManager = LocalFocusManager.current

        if(showDropDown) {
            LazyColumn {
                items(filteredDogBreedList) { item ->
                    Text(
                        text = item.name,
                        color = Purple_500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                text = item.name
                                searched = true
                                focusManager.clearFocus()

                                onSearchButtonClicked(item.name)
                            }
                            .padding(16.dp)
                    )
                    Divider(color = Purple_500)
                }
            }
        }
    }
}
