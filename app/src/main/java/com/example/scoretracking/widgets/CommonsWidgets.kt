package com.example.scoretracking.widgets

import android.graphics.fonts.FontFamily
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoretracking.R
import java.util.*
import com.example.scoretracking.R.string as AppText
import com.example.scoretracking.R.drawable as AppIcon


@Composable
fun EmailField(value: String,  onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(AppText.login_email)) },
        leadingIcon = {Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun PasswordField(value : String, onNewValue: (String) -> Unit, modifier: Modifier) {
    PasswordField(
        value = value,
        placeholder = AppText.login_password,
        onNewValue = { onNewValue(it) },
        modifier = modifier)
}

@Composable
fun RepeatPasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, AppText.login_repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon = if (isVisible) painterResource(AppIcon.ic_visibility_on)
                else painterResource(AppIcon.ic_visibility_off)

    val visualTransformation = if (isVisible) VisualTransformation.None
                                else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}



@Composable
fun BasicTextButton(
    @StringRes text: Int,
    modifier: Modifier,
    size : TextUnit,
    color : Color,
    action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) {
        Text(
            text = stringResource(text),
            fontSize = size,
            color = color)
    }
}

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun StaticsItem(
    statisticName : String = "Ball Possession",
    homeValue : String = "30",
    awayValue : String = "70",
) {
    var totalCount = homeValue.toInt() + awayValue.toInt()
    var percentToAppend = ""
    if (statisticName.lowercase().contains("possession") ||
        statisticName.lowercase().contains("%")) {
        totalCount = 100
        percentToAppend = "%"
    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
            ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(10.dp)) {
            Text(
                text = statisticName ,
                fontSize = 15.sp,
                textAlign = TextAlign.Center)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 5.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "$homeValue$percentToAppend")
                    StatisticBar(
                        value = homeValue.toInt(),
                        totalCount = totalCount,
                        isHome = true
                    )    
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.End){
                    Text(
                        modifier = Modifier.padding(end = 2.dp),
                        text = "$awayValue$percentToAppend",
                        textAlign = TextAlign.End
                    )
                    StatisticBar(
                        value = awayValue.toInt(),
                        totalCount = totalCount,
                        isHome = false
                    )
                }
            }
        }
    }
}


@Composable
fun StatisticBar (
    value : Int = 9,
    totalCount : Int = 12,
    isHome : Boolean = true
) {
    var percentage = 0f
    percentage = if (totalCount != 100) {
        (((value.toDouble()) / totalCount.toDouble())).toFloat()
    } else {
        value / 100f
    }
    LinearProgressIndicator(
        modifier = Modifier
            .graphicsLayer {
                if (isHome) {
                    rotationY = 180f
                }
            },
        color = if (isHome) colorResource(id = R.color.example_3_blue) else colorResource(id = R.color.green_500),
        backgroundColor = colorResource(id = R.color.example_4_grey_past),
        progress = percentage
    )
}