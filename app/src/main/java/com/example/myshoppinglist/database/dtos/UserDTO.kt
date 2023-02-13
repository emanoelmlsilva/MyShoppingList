package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.entities.User

class UserDTO() {

    var name: String = ""
    var nickName: String = ""
    var idAvatar:Int = R.drawable.default_avatar

    constructor(user: User):this(){
        name = user.name
        nickName = user.nickName
        idAvatar = user.idAvatar
    }
}