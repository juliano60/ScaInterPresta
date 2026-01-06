package com.nanoporetech.scainternew.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.model.LoginViewModel

@Composable
fun LoginScreen(
    model: LoginViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // HEADER SECTION
        HeaderAndLogo(
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // WELCOME MESSAGE
        Text(text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold
        )
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
                .width(dimensionResource(R.dimen.icon_large))
        )

        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Preview(
    //locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun LoginScreenPreview() {
    Surface(modifier = Modifier
        .fillMaxSize()) {
        LoginScreen(
            navController = rememberNavController(),
            model = viewModel(),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxSize()
        )
    }
}