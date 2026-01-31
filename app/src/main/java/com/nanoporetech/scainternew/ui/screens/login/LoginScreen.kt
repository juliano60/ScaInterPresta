package com.nanoporetech.scainternew.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme

@Composable
fun LoginScreen(
    onSubmit: () -> Unit,
    newUsername: String,
    newPassword: String,
    isLoginInvalid: Boolean,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onForgottenPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            // HEADER SECTION
            HeaderAndLogo(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // WELCOME MESSAGE SECTION
            WelcomeMessage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_large))
            )

            // CREDENTIALS SECTION
            CredentialsSection(
                username = newUsername,
                password = newPassword,
                onUsernameChanged = onUsernameChanged,
                onPasswordChanged = onPasswordChanged,
                onSubmit = onSubmit,
                isLoginInvalid = isLoginInvalid,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // FORGOTTEN PASSWORD SECTION
            ForgottenPasswordSection(
                onForgottenPassword = onForgottenPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_medium))
            )

            // LOGIN BUTTON SECTION
            Button(
                onClick = onSubmit,
                enabled = isFormFilledIn(newUsername, newPassword),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}

private fun isFormFilledIn(
    newUsername: String,
    newPassword: String
) : Boolean {
    return newUsername.isNotBlank() &&
            newPassword.isNotBlank()
}

@Composable
fun HeaderAndLogo(
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.sca_logo_no_title),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(R.dimen.icon_large))
                .width(dimensionResource(R.dimen.icon_large)),
        )

        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun WelcomeMessage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CredentialsSection(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoginInvalid: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = username,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            },
            label = { Text(stringResource(R.string.username_label)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            ),
            onValueChange = { onUsernameChanged(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            visualTransformation = VisualTransformation.None,
            isError = isLoginInvalid,
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            label = { Text(stringResource(R.string.password_label)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmit() },
            ),
            onValueChange = { onPasswordChanged(it) },
            visualTransformation = PasswordVisualTransformation(),
            isError = isLoginInvalid,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
fun ForgottenPasswordSection(
    onForgottenPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Switch(
                checked = false,
                onCheckedChange = { },
                modifier = Modifier
            )
            Text(
                text = stringResource(R.string.remember_me),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
                )
        }

        Text(
            text = stringResource(R.string.password_forgotten),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = {
                        onForgottenPassword()
                    }
                )
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun LoginScreenPreview(
) {
    ScaInterNewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LoginScreen(
                onSubmit = {},
                newUsername = "",
                newPassword = "",
                isLoginInvalid = false,
                onUsernameChanged = {},
                onPasswordChanged = {},
                onForgottenPassword = {},
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}