package com.example.myshoppinglist.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.ui.theme.*

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun CreateUser(navController: NavController?) {
    val createUserViewModel: CreateUserViewModel = viewModel()

    Surface(
        color = MaterialTheme.colors.background,
        contentColor = contentColorFor(secondary),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(secondary)
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderText()
            HeaderImage(createUserViewModel)
            ContentAvatares(createUserViewModel)
            TextFieldContent(createUserViewModel)
            ButtonContent(createUserViewModel)
        }

    }
}

@Composable
fun HeaderText() {
    val textWelcome = "Bem Vindo"
    val textBody = " ao seu gerenciado de compras.\n" +
            "\n" +
            "Aqui você vai poder ver todos os seus gastos durante os meses.\n" +
            "\n" +
            "Vai poder visualizar de uma forma simples quais os produtos que você mais utiliza."
    Text(
        text = AnnotatedString(
            text = textWelcome,
            spanStyle = SpanStyle(text_primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        ).plus(
            AnnotatedString(text = textBody, spanStyle = SpanStyle(text_primary, fontSize = 12.sp))
        )
    )

}

@Composable
fun HeaderImage(createUserViewModel: CreateUserViewModel) {
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val idAvatar: Int by createUserViewModel.idAvatar.observeAsState(initial = R.drawable.clover)
    Log.d("TEST", "atual $idAvatar")
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Image(
            painterResource(idAvatar),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .border(2.dp, text_primary, CircleShape)
        )
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.Center) {
            Text(text = name, Modifier.padding(0.dp, 8.dp))
            Text(text = nickName)
        }
    }

}

@ExperimentalFoundationApi
@Composable
fun ContentAvatares(createUserViewModel: CreateUserViewModel){
    val idAvatarCurrent: Int by createUserViewModel.idAvatar.observeAsState(R.drawable.default_avatar)

    var idsAvatar: List<Int> = listOf(R.drawable.clover, R.drawable.docinho, R.drawable.flapjack, R.drawable.jack, R.drawable.kuki, R.drawable.mabel, R.drawable.marceline, R.drawable.patolino, R.drawable.san, R.drawable.snoopy, R.drawable.default_avatar)

    Box(
        Modifier
            .shadow(1.dp, RoundedCornerShape(8.dp))
            .background(secondary)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)

        ) {
            Text(
                text = "Avatares:",
                Modifier.padding(4.dp, 0.dp, 0.dp, 4.dp),
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                cells = GridCells.Fixed(6),
                contentPadding = PaddingValues(3.dp)
            ) {
                items(idsAvatar) { idAvatar ->
                    Image(
                        painterResource(idAvatar),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(3.dp)
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                (if (idAvatarCurrent == idAvatar) primary else text_primary),
                                CircleShape
                            )
                            .clickable {
                                Log.d("TEST", "$idAvatar")
                                createUserViewModel.onChangeIdAvatar(idAvatar)
                            }
                    )
                }

            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(createUserViewModel: CreateUserViewModel){
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    var isErrorName by remember { mutableStateOf(false)}
    var isErrorNickName by remember { mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current

    Column (Modifier.height(180.dp).fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween){
        Column{
            OutlinedTextField(
                value = name,
                onValueChange = {
                    isErrorName = it.isBlank()
                    createUserViewModel.onChangeName(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = isErrorName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }

        Column{
            OutlinedTextField(
                value = nickName,
                onValueChange = {
                    isErrorNickName = it.isBlank()
                    createUserViewModel.onChangeNickName(it)
                                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Apelido") },
                singleLine = true,
                isError = isErrorNickName,
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }
    }

}

@Composable
fun ButtonContent(createUserViewModel: CreateUserViewModel){
    Row(Modifier.fillMaxWidth().padding(0.dp, 12.dp), horizontalArrangement = Arrangement.SpaceBetween){
        TextButton(
            modifier = Modifier.fillMaxWidth(0.4F).background(secondary)
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
            onClick = { /*Nothing*/ }) {
            Text("Cancelar", color = text_primary)
        }

        Button(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
            onClick = { /*Nothing*/ }) {
            Text("PROXIMO")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Proximo",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )

        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CreateUser(null)
}

class CreateUserViewModel: ViewModel(){

    var name: MutableLiveData<String> = MutableLiveData("")
    var idAvatar: MutableLiveData<Int> = MutableLiveData(R.drawable.default_avatar);
    var nickName: MutableLiveData<String> = MutableLiveData("")

    fun onChangeIdAvatar(newIdAvatar: Int){
        idAvatar.value = newIdAvatar
    }

    fun onChangeName(newName: String){
        name.value = newName
    }

    fun onChangeNickName(newNickName: String){
        nickName.value = newNickName
    }

}