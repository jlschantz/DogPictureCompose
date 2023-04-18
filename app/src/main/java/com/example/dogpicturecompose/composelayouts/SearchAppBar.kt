package com.example.dogpicturecompose.composelayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dogpicturecompose.R
import com.example.dogpicturecompose.ui.theme.Purple_500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onCloseIconClicked: () -> Unit,
    onSearchButtonClicked: (String) -> Unit,
    onValueChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple_500)
                .padding(4.dp),
            value = text,
            onValueChange = { newText ->
                text = newText
                onValueChange(text)
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
    }
}
