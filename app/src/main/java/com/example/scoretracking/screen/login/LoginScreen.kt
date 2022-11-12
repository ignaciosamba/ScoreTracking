package com.example.scoretracking.screen.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.commons.basicButton
import com.example.scoretracking.commons.fieldModifier
import com.example.scoretracking.commons.textButton
import com.example.scoretracking.commons.textButtonEnd
import com.example.scoretracking.navigation.SportTrackerScreens
import com.example.scoretracking.widgets.BasicButton
import com.example.scoretracking.widgets.BasicTextButton
import com.example.scoretracking.widgets.EmailField
import com.example.scoretracking.widgets.PasswordField
import kotlinx.coroutines.delay
import com.example.scoretracking.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(openAndPopUp: (String, String) -> Unit,
                openScreen: (String) -> Unit,
                viewModel: LoginScreenViewModel = hiltViewModel()) {

    DisposableEffect(viewModel) {
        onDispose { viewModel.removeListener() }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val uiState by viewModel.uiState

    val loginClicked = remember {
        viewModel.clicked
    }

    if (loginClicked.value) {
        LoginAlert()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

        Row(horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(1f))
            BasicTextButton(AppText.forgot_password, Modifier.textButtonEnd(), 13.sp, Color.DarkGray) {
                viewModel.onForgotPasswordClick()
            }
        }
        BasicButton(AppText.login, Modifier.basicButton()) {
            keyboard?.hide()
            loginClicked.value = false
            viewModel.onSignInClick(openAndPopUp)
        }
        BasicTextButton(AppText.login_register, Modifier.textButton(), 16.sp, Color.Black) {
            viewModel.onCreateAccountClicked(openScreen)
        }


    }

}

@Composable
fun LoginAlert () {
    AlertDialog(onDismissRequest = {},
        text = {
            Text(text = "Log In...", fontSize = 17.sp)
        },
        buttons = {})
}


