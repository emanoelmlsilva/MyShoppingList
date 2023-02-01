package com.example.myshoppinglist.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myshoppinglist.callback.CallbackItemList
import com.example.myshoppinglist.callback.CustomTextFieldOnClick
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.relations.ItemListAndCategory
import com.example.myshoppinglist.enums.TypeProduct
import com.example.myshoppinglist.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class SwipeDirection(val raw: Int) {
    Left(0),
    Initial(1),
    Right(2),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlidingItemListComponent(
    context: Context,
    itemListAndCategory: ItemListAndCategory,
    isCheck: Boolean,
    isMarket: Boolean,
    sizeCheckCollection: Boolean = true,
    isRemoved: Boolean = false,
    idItem: Long,
    category: Category,
    product: String,
    price: Float? = 0F,
    quantOrKilo: String? = "",
    type: TypeProduct? = TypeProduct.QUANTITY,
    backgroundColor: Color,
    callback: CallbackItemList?,
    callbackPrice: CustomTextFieldOnClick? = null,
    callbackQuantOrKilo: CustomTextFieldOnClick? = null
) {
    val squareSize = 100f
    val slidindState = rememberSwipeableState(SwipeDirection.Initial)
    val scope = rememberCoroutineScope()
    var enabledDeleteDialog by remember { mutableStateOf(false) }

    if (slidindState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (slidindState.currentValue) {
                    SwipeDirection.Right -> {
                        callback?.onUpdate(itemListAndCategory)
                    }
                    SwipeDirection.Left -> {
                        enabledDeleteDialog = true
                    }
                    else -> {
                        return@onDispose
                    }
                }
                scope.launch {
                    slidindState.animateTo(SwipeDirection.Initial)
                }
            }
        }
    }

    fun doToPix(dpValue: Float): Float {
        return dpValue * context.resources.displayMetrics.density
    }

    val anchors = mapOf(
        0f to SwipeDirection.Initial,
        -doToPix(squareSize) to SwipeDirection.Left,
        doToPix(squareSize) to SwipeDirection.Right
    )

    val modifierSwipeable = Modifier.swipeable(
        state = slidindState,
        anchors = anchors,
        thresholds = { _, _ -> FractionalThreshold(0.3f) },
        orientation = Orientation.Horizontal
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .then(if(!isMarket && sizeCheckCollection) modifierSwipeable else Modifier)
            .background(text_secondary)
    ) {
        if(!isMarket && sizeCheckCollection){
            Box() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .background(background_card)
                            .fillMaxWidth(.6f)
                            .fillMaxHeight()
                            .padding(start = 4.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("EDITAR", fontFamily = LatoRegular)
                    }
                    Column(
                        modifier = Modifier
                            .background(message_error)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(end = 4.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("REMOVER", fontFamily = LatoRegular)
                    }

                }

            }
        }
        if (enabledDeleteDialog) {
            Dialog(
                onDismissRequest = { },
                content = {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = text_secondary,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .background(text_secondary)

                        ) {

                            Row(
                                modifier = Modifier
                                    .background(text_title_secondary)
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Warning,
                                    contentDescription = null,
                                    tint = text_secondary
                                )

                                Text(
                                    "Deseja remover o item $product da lista?",
                                    fontFamily = LatoRegular
                                )

                            }

                            ButtonsFooterContent(
                                isClickable = true,
                                btnTextCancel = "CANCELAR",
                                btnTextAccept = "SALVAR",
                                onClickCancel = {
                                    enabledDeleteDialog = false
                                },
                                onClickAccept = {
                                    callback?.onDelete()
                                    enabledDeleteDialog = false
                                })
                        }
                    }
                })
        }
        BoxItemListComponent(
            modifier = Modifier.offset {
                IntOffset(
                    slidindState.offset.value.roundToInt(),
                    0
                )
            },
            context,
            isMarket,
            isCheck,
            isRemoved,
            idItem,
            category,
            product,
            backgroundColor,
            price,
            quantOrKilo,
            type,
            callback,
            callbackPrice,
            callbackQuantOrKilo
        )
    }
}