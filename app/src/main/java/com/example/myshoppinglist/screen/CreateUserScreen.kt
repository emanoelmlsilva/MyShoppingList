package com.example.myshoppinglist.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.components.ButtonsFooterContent
import com.example.myshoppinglist.components.TextInputComponent
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.viewModels.BaseFieldViewModel
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.UserViewModel
import com.example.myshoppinglist.enums.Screen
import com.example.myshoppinglist.model.UserInstanceImpl
import com.example.myshoppinglist.ui.theme.*


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun CreateUserScreen(navController: NavController?) {
    var createUserViewModel: CreateUserFieldViewModel = viewModel()
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val idAvatar: Int by createUserViewModel.idAvatar.observeAsState(0)
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val userViewModel: UserViewModel = UserViewModel(context)
    val categoryViewModel: CategoryViewModel = CategoryViewModel(context, lifecycleOwner)
    val categoryCollections = listOf(
        Category("Higiene", "outline_sanitizer_black_36.png", card_blue.toArgb()),
        Category("Limpeza", "outline_cleaning_services_black_36.png", card_pink.toArgb()),
        Category("Comida", "food_bank.png", card_red_dark.toArgb()),
        Category("Bebida", "outline_water_drop_black_36.png", card_orange.toArgb())
    )

    fun saveUser() {
        if (createUserViewModel.checkFileds()) {
            userViewModel.insertUser(User(name.trim(), nickName.trim(), idAvatar))
            categoryCollections.forEach {
                val category = Category(it.category, it.idImage, it.color)
                categoryViewModel.insertCategory(category)
            }
            UserInstanceImpl.reset()
            navController?.navigate("${Screen.CreateCards.name}?hasToolbar=${false}?nameUser=${name.trim()}")
        }
    }

    TopAppBarScreen(isScrollable = true, content = {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

           Column{
               HeaderText()
               HeaderImage(createUserViewModel)
               ContentAvatares(createUserViewModel)
               TextFieldContent(createUserViewModel, object : Callback {
                   override fun onClick() {
                       saveUser()
                   }
               })
           }
            ButtonsFooterContent(
                btnTextAccept = "PROXIMO",
                iconAccept = Icons.Filled.ArrowForward,
                onClickAccept = {
                    saveUser()
                })
        }
    })
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
            AnnotatedString(text = textBody, spanStyle = SpanStyle(text_primary, fontSize = 14.sp))
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
            .padding(4.dp, 26.dp, 0.dp, 0.dp)
    ) {
        Image(
            painterResource(idAvatar),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(78.dp)
                .clip(CircleShape)
                .border(2.dp, text_primary, CircleShape)
        )
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.Center) {
            Text(
                text = if (name.isBlank()) "Nome" else name,
                Modifier.padding(0.dp, 8.dp),
                color = if (name.isBlank()) secondary_dark else text_primary
            )
            Text(
                text = if (nickName.isBlank()) "Apelido" else nickName,
                color = if (nickName.isBlank()) secondary_dark else text_primary
            )
        }
    }

}

@ExperimentalFoundationApi
@Composable
fun ContentAvatares(createUserViewModel: CreateUserFieldViewModel) {
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    var idAvatarCurrent: Int by remember {
        mutableStateOf(R.drawable.clover)
    }

    createUserViewModel.idAvatar.observe(lifecycleOwner) {
        idAvatarCurrent = it
    }

    val idsAvatar = remember {
        mutableStateListOf(
            R.drawable.default_avatar,
            R.drawable.clover,
            R.drawable.docinho,
            R.drawable.flapjack,
            R.drawable.kuki,
            R.drawable.marceline,
            R.drawable.patolino,
            R.drawable.san,
            R.drawable.snoopy
        )
    }

    Box(
        Modifier
            .padding(vertical = 26.dp)
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
            Column {
                Row {
                    idsAvatar.subList(0, 5).map { idAvatar ->
                        itemImage(idAvatar, idAvatarCurrent, createUserViewModel)

                    }
                }
                Row(modifier = Modifier.padding(bottom = 6.dp)) {
                    idsAvatar.subList(5, idsAvatar.size).map { idAvatar ->
                        itemImage(idAvatar, idAvatarCurrent, createUserViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun itemImage(idAvatar: Int, idAvatarCurrent: Int, createUserViewModel: CreateUserFieldViewModel) {
    Image(
        painter = painterResource(id = idAvatar),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(3.dp)
            .size(50.dp)
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

@ExperimentalComposeUiApi
@Composable
fun TextFieldContent(createUserViewModel: CreateUserFieldViewModel, callback: Callback) {
    val name: String by createUserViewModel.name.observeAsState("")
    val nickName: String by createUserViewModel.nickName.observeAsState(initial = "")
    val isErrorName: Boolean by createUserViewModel.isErrorName.observeAsState(false)
    val isErrorNickName: Boolean by createUserViewModel.isErrorNickName.observeAsState(false)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextInputComponent(modifier = Modifier.fillMaxWidth(),
            value = name,
            label = "Nome",
            isMandatory = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            error = isErrorName,
            customOnClick = object : CustomTextFieldOnClick {
                override fun onChangeValue(newValue: String) {
                    createUserViewModel.onChangeName(newValue)
                }
            })

        TextInputComponent(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            value = nickName,
            label = "Apelido",
            isMandatory = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                    keyboardController?.hide()
                    callback.onClick()
                }
            ),
            error = isErrorNickName,
            customOnClick = object : CustomTextFieldOnClick {
                override fun onChangeValue(newValue: String) {
                    createUserViewModel.onChangeNickName(newValue)
                }
            })
    }

}

class CreateUserFieldViewModel : BaseFieldViewModel() {

    var name: MutableLiveData<String> = MutableLiveData("")
    var idAvatar: MutableLiveData<Int> = MutableLiveData(R.drawable.clover)
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