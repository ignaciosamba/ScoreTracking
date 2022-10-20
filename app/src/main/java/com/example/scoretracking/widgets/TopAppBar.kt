package com.example.scoretracking.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scoretracking.R


sealed class MenuAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int) {

    object go : MenuAction(R.string.go_action_icon, R.drawable.ic_arrow_left)
    object search : MenuAction(R.string.search_action_icon, R.drawable.ic_search)
}