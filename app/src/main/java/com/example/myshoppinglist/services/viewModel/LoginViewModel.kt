package com.example.myshoppinglist.services.viewModel

import ResultData
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.callback.CallbackObject
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.database.viewModels.UserViewModelDB
import com.example.myshoppinglist.enums.StatusSaveData
import com.example.myshoppinglist.services.repository.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val userViewModel: UserViewModelDB
) : ViewModel() {

    private val TAG = "LoginViewModel"

    fun updateUser(userDTO: UserDTO, callback: CallbackObject<UserDTO>) {
        viewModelScope.launch {
            val resultUpdate = try {
                loginRepository.updateUser(userDTO)
            } catch (exception: Exception) {

                callback.onChangeStatus(StatusSaveData.ERROR)
                delay(2000L)

                when (exception) {
                    is ConnectException -> {
                        ResultData.NotConnectionService(userDTO)
                    }
                    is SocketTimeoutException -> {
                        ResultData.NotConnectionService(userDTO)
                    }
                    else -> {
                        ResultData.Error(exception)
                    }
                }
            }

            when (resultUpdate) {
                is ResultData.Success -> {
                    callback.onChangeStatus(StatusSaveData.UPDATE)
                    delay(1000L)

                    userViewModel.updateUser(userDTO.fromUser())

                    callback.onSuccess(userDTO)
                }
                is ResultData.NotConnectionService -> {
                    callback.onChangeStatus(StatusSaveData.UPDATE)
                    delay(1000L)

                    val userData = resultUpdate.data

                    Log.d(TAG, "updateUser $userData")

                    userViewModel.updateUser(userData.fromUser())

                    callback.onSuccess(userData)
                }
                else -> {
                    val messageError = (resultUpdate as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")
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
                    Log.d(TAG, "user $user")

                    userViewModel.insertUser(user.fromUser())

                    callback.onSuccess(user)
                }
                else -> {
                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")
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
                    Log.d(TAG, "result ${result.data}")

                    userViewModel.insertUser(user.fromUser())

                    callback.onSuccess()
                }
                else -> {

                    val messageError = (result as ResultData.Error).exception.message

                    Log.d(TAG, "error $messageError")
                    callback.onFailedException(result.exception)
                }
            }
        }
    }
}