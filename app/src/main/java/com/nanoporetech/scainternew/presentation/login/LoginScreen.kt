package com.nanoporetech.scainternew.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    newUsername: String,
    newPassword: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
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

            // WELCOME MESSAGE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_large))
            ) {
                Text(
                    text = stringResource(R.string.welcome_message),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }

            // CREDENTIALS SECTION
            CredentialsSection(
                username = newUsername,
                password = newPassword,
                onUsernameChanged = onUsernameChanged,
                onPasswordChanged = onPasswordChanged,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // FORGOTTEN PASSWORD AREA
            ForgottenPasswordSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_medium))
            )

            Button(
                onClick = onLogin,
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
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun CredentialsSection(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ) {
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
            onValueChange = { onPasswordChanged(it) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
fun ForgottenPasswordSection(
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
                fontSize = 16.sp
                )
        }

        Text(
            text = stringResource(R.string.password_forgotten),
            fontSize = 16.sp
        )
    }
}

@Preview(
    //locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun LoginScreenPreview(
) {
    Surface(modifier = Modifier
        .fillMaxSize()) {
        LoginScreen(
            onLogin = {},
            newUsername = "",
            newPassword = "",
            onUsernameChanged = {},
            onPasswordChanged = {},
            modifier = Modifier
                .background(AppConstants.lightGreen)
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}