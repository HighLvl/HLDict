package com.example.onscreendictionary.ui.main

import android.os.Parcelable
import com.example.onscreendictionary.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MainBottomSection(
    val menuItemId: Int
): Parcelable {
    Search(R.id.mainSearchItem),
    //Reminder(R.id.mainReminderItem),
    Favorites(R.id.mainFavoriteItem)
    ;

    companion object {
        fun getByMenuItemId(id: Int): MainBottomSection? {
            return values().find { it.menuItemId == id }
        }
    }
}