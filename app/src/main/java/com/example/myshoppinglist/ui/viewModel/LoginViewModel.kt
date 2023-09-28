package com.example.myshoppinglist.ui.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.Callback
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.viewModels.UserViewModelDB
import com.example.myshoppinglist.services.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository, private val userViewModel: UserViewModelDB) : ViewModel() {

    private val LOG = "LoginViewModel"

    fun updateUser(userDTO: UserDTO, callback: CallbackObject<UserDTO>){
        viewModelScope.launch {

            val resultUpdate = try{
                loginRepository.updateUser(userDTO)
            }catch (exception: Exception){
                ResultData.Error(Exception(exception.message))
            }

            when(resultUpdate){
                is ResultData.Success -> {
                    userViewModel.updateUser(userDTO.fromUser())

                    callback.onSuccess(userDTO)
                }
                else -> {
                    val messageError = (resultUpdate as ResultData.Error).exception.message

                    Log.d(LOG, "error $messageError")
                    callback.onFailed(messageError.toString())
                }
            }
        }
    }

    fun login(email: String, password: String, callback: CallbackObject<UserDTO>) {

        viewModelScope.launch {

            val result = try {
                loginRepository.login(email, password)
            } catch (e: Exception) {
                ResultData.Error(e)
            }

            when (result) {
                is ResultData.Success<UserDTO> -> {
                    val user = result.data
                    Log.d(LOG, "user $user")

                    userViewModel.insertUser(user.fromUser())

                    callback.onSuccess(user)
                }
                else -> {

                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(LOG, "error $messageError")
                    callback.onFailedException(result.exception)
                }
            }
        }

    }

    fun singUp(user: UserDTO, callback: CallbackObject<UserDTO>) {
        viewModelScope.launch {

            val result = try {
                loginRepository.signUp(user)
            } catch (e: Exception) {
                ResultData.Error(e)
            }

            when (result) {
                is ResultData.Success<UserDTO> -> {
                    Log.d(LOG, "result ${result.data}")

                    userViewModel.insertUser(user.fromUser())

                    callback.onSuccess()
                }
                else -> {

                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(LOG, "error $messageError")
                    callback.onFailedException(result.exception)
                }
            }
        }
    }
}