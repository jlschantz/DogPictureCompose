package com.example.dogpicturecompose.composelayouts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogpicturecompose.R

@Composable
fun TopAppBar(onSearchIconClicked: () -> Unit) {

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