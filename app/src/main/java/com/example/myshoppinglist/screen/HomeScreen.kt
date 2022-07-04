package com.example.myshoppinglist.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.myshoppinglist.components.CreateHeaderFieldViewModel
import com.example.myshoppinglist.components.HeaderComponent
import com.example.myshoppinglist.components.SpendingComponent
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.text_secondary

@Composable
fun HomeScreen(navController: NavController?) {
    val context = LocalContext.current

    var userViewModel = UserViewModel(context)
    var createHeaderFieldViewModel = CreateHeaderFieldViewModel()

    userViewModel.searchResult.observeForever { user ->
        createHeaderFieldViewModel.onChangeNameUser(user.name)

        createHeaderFieldViewModel.onchangeIdAvatar(user.idAvatar)
    }

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(text_secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            HeaderComponent(createHeaderFieldViewModel)
            SpendingComponent()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = null)
}