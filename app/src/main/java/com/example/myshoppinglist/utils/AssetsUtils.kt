package com.example.myshoppinglist.utils

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.example.myshoppinglist.model.IconCategory
import java.io.IOException
import java.io.InputStream


object AssetsUtils {

    @JvmStatic
    fun readIconCollections(context: Context): List<IconCategory> {
        return context.assets.list("icons")?.map {
            try {
                val ims: InputStream = context.assets.open("icons/${it}")
                val drawable = Drawable.createFromStream(ims, null)
                IconCategory(it, drawable!!.toBitmap())
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList<IconCategory>()
            }

        } as List<IconCategory>
    }

    @JvmStatic
    fun readIconBitmapById(context: Context, id: String): Bitmap? {
        val ims: InputStream = context.assets.open("icons/${id}")
        val drawable = Drawable.createFromStream(ims, null)
        return drawable!!.toBitmap()
    }

    @JvmStatic
    fun readIconImageBitmapById(context: Context, id: String): ImageBitmap? {
        return readIconBitmapById(context, id)!!.asImageBitmap()
    }

    @JvmStatic
    fun readIconCategoryById(context: Context, id: String): IconCategory? {
        val bitmap = readIconBitmapById(context, id)
        return IconCategory(id, bitmap!!)
    }
}