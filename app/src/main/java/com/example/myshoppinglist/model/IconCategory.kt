package com.example.myshoppinglist.model

import android.graphics.Bitmap

class IconCategory() {
    var idImage: String = ""
    var imageBitmap: Bitmap? = null

    constructor(idImage: String, imageBitmap: Bitmap) : this() {
        this.idImage = idImage
        this.imageBitmap = imageBitmap
    }

}