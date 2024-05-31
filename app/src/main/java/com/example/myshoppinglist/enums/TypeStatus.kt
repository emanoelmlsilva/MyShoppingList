package com.example.myshoppinglist.enums

import com.example.myshoppinglist.R

enum class TypeStatus {

    DELETE, UPDATE, TRANSFER, SAVE;

    fun getRawStatus(): Int {
        return when (this){
            UPDATE -> R.raw.update_full
            TRANSFER -> R.raw.transfer_full
            DELETE -> R.raw.delete_full
            else -> {
                R.raw.save
            }
        }
    }
}