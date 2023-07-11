package com.example.myshoppinglist.services.repository

import ResultData
import android.util.Log
import com.example.myshoppinglist.database.dtos.UserDTO
import com.example.myshoppinglist.services.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository(private val userService: UserService) {

    private val LOG = "LoginRepository"

    suspend fun updateUser(userDTO: UserDTO): ResultData<UserDTO> {
        return withContext((Dispatchers.IO)) {
            val userExecute = userService.update(userDTO).execute()
            return@withContext if (userExecute.isSuccessful) {
                Log.d(
                    LOG,
                    "success = $userExecute , user ${userExecute.body().toString()}"
                )

                val userResponse = userExecute.body()

                ResultData.Success(userResponse ?: UserDTO())

            } else {
                ResultData.Error(Exception("ERROR UPDATE ${userExecute.errorBody()}"))
            }
        }
    }

    suspend fun login(email: String, password: String): ResultData<UserDTO> {
        return withContext(Dispatchers.IO) {
            val userExecute = userService.findUser(email, password).execute()
            return@withContext if (userExecute.isSuccessful) {
                Log.d(
                    LOG,
                    "success = $userExecute , user ${userExecute.body().toString()}"
                )
                val userResponse = userExecute.body()

                ResultData.Success(userResponse ?: UserDTO())

            } else {
                ResultData.Error(Exception("ERROR LOGIN ${userExecute.errorBody()}"))
            }
        }
    }

    suspend fun signUp(user: UserDTO): ResultData<UserDTO> {
        return withContext(Dispatchers.IO) {
            val userExecute = userService.save(user).execute()
            return@withContext if (userExecute.isSuccessful) {
                Log.d(
                    LOG,
                    "success = $userExecute , user ${userExecute.body().toString()}"
                )

                val userResponse = userExecute.body()

                ResultData.Success(userResponse ?: UserDTO())
            } else {
                ResultData.Error(Exception("ERROR SIGNUP ${userExecute.errorBody()}"))
            }
        }
    }
}