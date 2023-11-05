package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.entities.User
import com.google.gson.annotations.SerializedName

class UserDTO() {

    @SerializedName("email")
    var email: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("nickName")
    var nickName: String = ""
    @SerializedName("idAvatar")
    var idAvatar:Int = R.drawable.default_avatar
    @SerializedName("password")
    var password:String = ""

    constructor(user: User):this(){
        email = user.email
        name = user.name
        nickName = user.nickName
        idAvatar = user.idAvatar
    }

    constructor(email: String, password: String) : this(){
        this.email = email
        this.password = password
    }

    override fun toString(): String {
        return "UserDTO(email='$email', name='$name', nickName='$nickName', idAvatar=$idAvatar)"
    }


    fun fromUser() : User{
        return User(email, password, name, nickName, idAvatar)
    }
}