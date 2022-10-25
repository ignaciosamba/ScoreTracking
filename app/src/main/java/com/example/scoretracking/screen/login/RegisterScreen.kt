package com.example.scoretracking.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scoretracking.commons.basicButton
import com.example.scoretracking.commons.fieldModifier
import com.example.scoretracking.widgets.BasicButton
import com.example.scoretracking.widgets.EmailField
import com.example.scoretracking.widgets.PasswordField
import com.example.scoretracking.widgets.RepeatPasswordField
import com.example.scoretracking.R.string as AppText

@Composable
fun RegisterScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: RegisterScreenViewModel = hiltViewModel()){

    val uiState by viewModel.uiState
    
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(AppText.register_text),
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp
            )
        }

        EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())
        RepeatPasswordField(uiState.repeatPassword, viewModel::onPasswordRepeatChange, Modifier.fieldModifier())
        
        BasicButton(text = AppText.register_text, modifier = Modifier.basicButton()) {
            viewModel.onRegisterClicked(openAndPopUp)
        }
    }
}