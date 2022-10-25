package com.example.scoretracking.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


fun Modifier.textButtonEnd(): Modifier {
    return this.padding(start = 16.dp, end = 16.dp, top = 4.dp)
}

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
}