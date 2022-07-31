package com.example.myshoppinglist.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshoppinglist.R
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.ui.theme.primary
import com.example.myshoppinglist.ui.theme.secondary
import com.example.myshoppinglist.ui.theme.secondary_dark
import com.example.myshoppinglist.ui.theme.text_primary

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun CreateUserScreen(navController: NavController?) {
    val hasToobar: String = "false"
    var createUserViewModel: CreateUserFieldViewModel = viewModel()
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val idAvatar: Int by createUserViewModel.idAvatar.observeAsState(0)
    val context = LocalContext.current
    val userViewModel: UserViewModel = UserViewModel(context)

    fun saveUser() {
        if (createUserViewModel.checkFileds()) {
            userViewModel.insertUser(User(name, nickName, idAvatar))
            navController?.navigate("createCards?hasToolbar=${hasToobar.toBoolean()}")
        }
    }

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
            TextFieldContent(createUserViewModel, object : Callback {
                override fun onClick() {
                    saveUser()
                }
            })
            ButtonsFooterContent(
                btnTextAccept = "PROXIMO",
                btnTextCancel = "CANCELAR",
                Icons.Filled.ArrowForward, null,
                onClickAccept = {
                    saveUser()
                }, onClickCancel = {

                })
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
fun HeaderImage(createUserViewModel: CreateUserFieldViewModel) {
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val idAvatar: Int by createUserViewModel.idAvatar.observeAsState(initial = R.drawable.clover)

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
fun ContentAvatares(createUserViewModel: CreateUserFieldViewModel) {
    val idAvatarCurrent: Int by createUserViewModel.idAvatar.observeAsState(R.drawable.default_avatar)

    var idsAvatar: List<Int> = listOf(
        R.drawable.clover,
        R.drawable.docinho,
        R.drawable.flapjack,
        R.drawable.jack,
        R.drawable.kuki,
        R.drawable.mabel,
        R.drawable.marceline,
        R.drawable.patolino,
        R.drawable.san,
        R.drawable.snoopy,
        R.drawable.default_avatar
    )

    Box(
        Modifier
            .shadow(1.dp, RoundedCornerShape(8.dp))
            .background(secondary)
    ) {
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
fun TextFieldContent(createUserViewModel: CreateUserFieldViewModel, callback: Callback) {
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val isErrorName: Boolean by createUserViewModel.isErrorName.observeAsState(false)
    val isErrorNickName: Boolean by createUserViewModel.isErrorNickName.observeAsState(false)
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .height(180.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    createUserViewModel.onChangeName(it)
                },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = isErrorName,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }

        Column {
            OutlinedTextField(
                value = nickName,
                onValueChange = {
                    createUserViewModel.onChangeNickName(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Apelido") },
                singleLine = true,
                isError = isErrorNickName,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        callback.onClick()
                    }
                ),
            )
            Text(text = "Obrigatório", color = secondary_dark)
        }
    }

}

class CreateUserFieldViewModel : BaseFieldViewModel() {

    var name: MutableLiveData<String> = MutableLiveData("")
    var idAvatar: MutableLiveData<Int> = MutableLiveData(R.drawable.default_avatar)
    var nickName: MutableLiveData<String> = MutableLiveData("")
    var isErrorName: MutableLiveData<Boolean> = MutableLiveData(false)
    var isErrorNickName: MutableLiveData<Boolean> = MutableLiveData(false)

    fun onChangeIdAvatar(newIdAvatar: Int) {
        idAvatar.value = newIdAvatar
    }

    fun onChangeName(newName: String) {
        onChangeIsErrorName(newName.isBlank())
        name.value = newName
    }

    fun onChangeNickName(newNickName: String) {
        onChangeisErrorNickName(newNickName.isBlank())
        nickName.value = newNickName
    }

    fun onChangeIsErrorName(newIsErrorName: Boolean) {
        isErrorName.value = newIsErrorName
    }

    fun onChangeisErrorNickName(newIsErrorNickName: Boolean) {
        isErrorNickName.value = newIsErrorNickName
    }

    override fun checkFileds(): Boolean {

        if (name.value!!.isBlank()) {
            onChangeIsErrorName(true)
            return false
        }

        if (nickName.value!!.isBlank()) {
            onChangeisErrorNickName(true)
            return false
        }

        return true
    }
}