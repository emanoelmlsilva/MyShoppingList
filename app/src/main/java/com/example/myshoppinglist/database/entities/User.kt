package com.example.myshoppinglist.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myshoppinglist.R
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "users")
class User {

    @PrimaryKey
    @NotNull
    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String = ""

    @NotNull
    @SerializedName("password")
    @ColumnInfo(name = "password")
    var password: String = ""

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String = ""

    @ColumnInfo(name = "nickName")
    @SerializedName("nickName")
    var nickName: String = ""

    @ColumnInfo(name = "idAvatar")
    @SerializedName("idAvatar")
    var idAvatar:Int = R.drawable.default_avatar

    constructor()

    constructor(email: String, password: String): this() {
        this.email = email
        this.password = password
    }

    constructor(email: String, password: String, name: String, nickName: String, idAvatar: Int) {
        this.email = email
        this.password = password
        this.name = name
        this.nickName = nickName
        this.idAvatar = idAvatar
    }

    override fun toString(): String {
        return "User(email='$email', password='$password', name='$name', nickName='$nickName', idAvatar=$idAvatar)"
    }

}