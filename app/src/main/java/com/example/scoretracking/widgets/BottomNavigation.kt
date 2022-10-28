package com.example.scoretracking.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoretracking.R


sealed class BottomNavItem(var title : String, var icon : Int, var screen_route : String){

    //Favorites
    object Soccer : BottomNavItem("Football", R.drawable.ic_soccer,"Soccer")
    object Basketball: BottomNavItem("Basketball",R.drawable.ic_basketball,"Basketball")
    object MotorSport: BottomNavItem("MotorSport",R.drawable.ic_motorsport,"Motorsport")

    //Main Screen
    object Games : BottomNavItem("Games", R.drawable.ic_games,"Games")
    object Standings: BottomNavItem("Standings",R.drawable.ic_standings,"Standing")
    object News: BottomNavItem("News",R.drawable.ic_news,"News")

}


@Composable
fun BottomNavFavoritesApp(selected : String,
                          onClickEvent : (String) -> Unit) {
    val item = listOf(
        BottomNavItem.Soccer,
        BottomNavItem.Basketball,
        BottomNavItem.MotorSport
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.Black){
            item.forEach {
                BottomNavigationItem(
                    selected = selected == it.screen_route,
                    onClick = { onClickEvent(it.screen_route) },
                    icon = { Icon(painterResource(id = it.icon), contentDescription = it.title) },
                    label = { Text(text = it.title, fontSize = 9.sp) },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.LightGray)
            }
    }
}

@Composable
fun BottomNavMainApp(selected : String,
                          onClickEvent : (String) -> Unit) {
    val item = listOf(
        BottomNavItem.Games,
        BottomNavItem.Standings,
        BottomNavItem.News
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        modifier = Modifier.padding(top = 2.dp),
        contentColor = Color.Black){
        item.forEach {
            BottomNavigationItem(
                selected = selected == it.screen_route,
                onClick = { onClickEvent(it.screen_route) },
                icon = { Icon(painterResource(id = it.icon), contentDescription = it.title) },
                label = { Text(text = it.title, fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray)
        }
    }
}