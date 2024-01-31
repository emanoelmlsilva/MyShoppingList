package com.example.myshoppinglist.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.myshoppinglist.R

import androidx.compose.material.*

@SuppressLint("MissingInflatedId")
fun CustomToastComponent(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.layout_toast, null)

    val textView = layout.findViewById<TextView>(R.id.textViewMessage)
    textView.text = message

    val toast = Toast(context)
    toast.duration = duration
    toast.view = layout
    toast.show()
}