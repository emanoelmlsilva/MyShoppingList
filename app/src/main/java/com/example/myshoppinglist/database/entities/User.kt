package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.R
import org.jetbrains.annotations.NotNull

@Entity(tableName = "users")
class User {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "nickName")
    var nickName: String = ""

    @ColumnInfo(name = "idAvatar")
    var idAvatar = R.drawable.default_avatar

    constructor()

    constructor(name: String, nickName: String, idAvatar: Int) {
        this.name = name
        this.nickName = nickName
        this.idAvatar = idAvatar
    }

    override fun toString(): String {
        return "User(name='$name', nickName='$nickName', idAvatar=$idAvatar)"
    }


}