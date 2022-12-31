package com.example.myshoppinglist.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

object CategoryIconsUtil {

    private val categoryCollection = listOf( Icons.Outlined.AccountCircle, Icons.Outlined.AccountBox, Icons.Outlined.AddCircle, Icons.Outlined.Add, Icons.Outlined.ArrowBack, Icons.Outlined.ArrowDropDown, Icons.Outlined.ArrowForward,
        Icons.Outlined.Build,
        Icons.Outlined.Call, Icons.Outlined.CheckCircle, Icons.Outlined.Check, Icons.Outlined.Clear, Icons.Outlined.Close, Icons.Outlined.Create,
        Icons.Outlined.Delete, Icons.Outlined.DateRange, Icons.Outlined.Done,
        Icons.Outlined.Edit, Icons.Outlined.Email, Icons.Outlined.ExitToApp)

    @JvmStatic
    fun getCategoryColleciton(): List<ImageVector>{
        return categoryCollection
    }

}