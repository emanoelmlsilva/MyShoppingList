package com.example.myshoppinglist.exception

class ErrorFormatException: Exception {

    constructor() : this("Erro ao converter String para Float")

    constructor(message: String?) : super(message)
}