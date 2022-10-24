package com.example.scoretracking.screen.login

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(popUpScreen: () -> Unit,
                viewModel: LoginScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState



}


