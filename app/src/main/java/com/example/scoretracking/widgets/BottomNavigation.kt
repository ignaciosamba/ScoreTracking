package com.example.scoretracking.widgets

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.scoretracking.R


sealed class BottomNavItem(var title : String, var icon : Int, var screen_route : String){

    object Soccer : BottomNavItem("Football", R.drawable.ic_soccer,"Soccer")
    object Basketball: BottomNavItem("Basketball",R.drawable.ic_basketball,"Basketball")
    object MotorSport: BottomNavItem("MotorSport",R.drawable.ic_motorsport,"Motorsport")
}


@Composable
fun BottomNavApp(selected : String,
                 onClickEvent : (String) -> Unit) {
    val item = listOf(
        BottomNavItem.Soccer,
        BottomNavItem.Basketball,
        BottomNavItem.MotorSport
    )

    BottomNavigation (
        backgroundColor = colorResource(id = R.color.purple_700),
        contentColor = Color.Black){
            item.forEach {
                BottomNavigationItem(
                    selected = selected == it.screen_route,
                    onClick = { onClickEvent(it.screen_route) },
                    icon = { Icon(painterResource(id = it.icon), contentDescription = it.title) },
                    label = { Text(text = it.title, fontSize = 9.sp) },
                    selectedContentColor = Color.Red,
                    unselectedContentColor = Color.White)
            }
    }
}