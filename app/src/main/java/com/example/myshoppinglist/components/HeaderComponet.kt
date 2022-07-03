package com.example.myshoppinglist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.ui.theme.*

@Composable
fun HeaderComponent(createHeaderFieldViewModel: CreateHeaderFieldViewModel){
    var isVisibleValue = createHeaderFieldViewModel.isVisibleValue.observeAsState(initial = true)

    Box(
        Modifier
            .background(primary_dark)
            .height(125.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, bottom = 3.dp, end = 8.dp)){
        Row(
            Modifier
                .fillMaxSize()){
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.75f), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painterResource(R.drawable.clover),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                )
                Text(text = "Ola, Emanoel", Modifier.padding(0.dp, 16.dp), color = text_secondary)
            }
            IconButton(onClick = { createHeaderFieldViewModel.onChangeIsVisibleValue()}) {
                Icon(
                    painter = painterResource(id = if(isVisibleValue.value) R.drawable.ic_baseline_visibility_24 else R.drawable.ic_baseline_visibility_off_24),
                    contentDescription = "Visible Values",
                    tint = text_secondary
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_help_outline_24),
                    contentDescription = "help",
                    tint = text_secondary
                )
            }
        }
    }
}

class CreateHeaderFieldViewModel: BaseFieldViewModel(){
    var isVisibleValue: MutableLiveData<Boolean> = MutableLiveData(true)

    fun onChangeIsVisibleValue(){
        isVisibleValue.value = !isVisibleValue.value!!
    }

    override fun checkFileds(): Boolean {
        TODO("Not yet implemented")
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHeaderComponent(){
    var createHeaderFieldViewModel = CreateHeaderFieldViewModel()
    HeaderComponent(createHeaderFieldViewModel)
}