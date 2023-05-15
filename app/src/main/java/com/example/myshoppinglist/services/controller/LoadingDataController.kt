package com.example.myshoppinglist.services.controller

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LifecycleOwner
import com.example.myshoppinglist.database.entities.Category
import com.example.myshoppinglist.database.entities.User
import com.example.myshoppinglist.database.entities.relations.UserWithCategory
import com.example.myshoppinglist.database.entities.relations.UserWithCreditCard
import com.example.myshoppinglist.database.viewModels.CategoryViewModel
import com.example.myshoppinglist.database.viewModels.CreditCardViewModel
import com.example.myshoppinglist.services.CategoryService
import com.example.myshoppinglist.services.CreditCardService
import com.example.myshoppinglist.ui.theme.card_blue
import com.example.myshoppinglist.ui.theme.card_orange
import com.example.myshoppinglist.ui.theme.card_pink
import com.example.myshoppinglist.ui.theme.card_red_dark
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingDataController {


    companion object {
        private lateinit var creditCardService: CreditCardService
        private lateinit var categoryService: CategoryService

        private lateinit var creditCardViewModel: CreditCardViewModel
        private lateinit var categoryViewModel: CategoryViewModel

        fun getData(context: Context, lifecycleOwner: LifecycleOwner) : LoadingDataController{
            creditCardViewModel = CreditCardViewModel(context, lifecycleOwner)
            categoryViewModel = CategoryViewModel(context, lifecycleOwner)

            creditCardService = CreditCardService.getCreditCardService()
            categoryService = CategoryService.getCategoryService()

            return LoadingDataController()
        }
    }

    fun loadingData(user: User, callback: com.example.myshoppinglist.callback.Callback) {

        creditCardService.findAll(user.email).enqueue(object : Callback<List<UserWithCreditCard>> {
            override fun onResponse(
                call: Call<List<UserWithCreditCard>>,
                response: Response<List<UserWithCreditCard>>
            ) {

                val creditCardCollection = response.body()

                creditCardCollection?.toObservable()?.subscribeBy(
                    onNext = { creditCardViewModel.insertCreditCard(it.toCreditCard())},
                    onError =  { callback.onCancel() },
                    onComplete = { loadingDataCategories(user.email, callback) }
                )
            }

            override fun onFailure(
                call: Call<List<UserWithCreditCard>>,
                t: Throwable
            ) {
                callback.onCancel()
            }
        })
    }

    fun loadingDataCategories(email: String, callback: com.example.myshoppinglist.callback.Callback){
        categoryService.findAll(email).enqueue(object : Callback<List<UserWithCategory>> {
            override fun onResponse(
                call: Call<List<UserWithCategory>>,
                response: Response<List<UserWithCategory>>
            ) {

                val categoryCollection = response.body()

                if(!categoryCollection?.isEmpty()!!){
                    categoryCollection.toObservable().subscribeBy(
                        onNext = { categoryViewModel.insertCategory(it.toCategory())},
                        onError =  { callback.onCancel() },
                        onComplete = { callback.onSucess() }
                    )
                }else{
                    listOf(
                        Category(email,"Higiene", "outline_sanitizer_black_36.png", card_blue.toArgb()),
                        Category(email,"Limpeza", "outline_cleaning_services_black_36.png", card_pink.toArgb()),
                        Category(email,"Comida", "food_bank.png", card_red_dark.toArgb()),
                        Category(email,"Bebida", "outline_water_drop_black_36.png", card_orange.toArgb())
                    ).toObservable().subscribeBy(
                        onNext = { categoryViewModel.insertCategory(it)},
                        onError =  { callback.onCancel() },
                        onComplete = { callback.onSucess() }
                    )
                }
            }

            override fun onFailure(
                call: Call<List<UserWithCategory>>,
                t: Throwable
            ) {
                callback.onCancel()
            }
        })
    }
}