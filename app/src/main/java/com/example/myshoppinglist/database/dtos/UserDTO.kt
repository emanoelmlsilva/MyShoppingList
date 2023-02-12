package com.example.myshoppinglist.database.dtos

import com.example.myshoppinglist.R
import com.example.myshoppinglist.database.entities.User

class UserDTO {

    var name: String = ""
    var nickName: String = ""
    var idAvatar:Int = R.drawable.resource_default

    constructor(user: User){
        name = user.name
        nickName = user.nickName
        idAvatar = user.idAvatar
    }
}