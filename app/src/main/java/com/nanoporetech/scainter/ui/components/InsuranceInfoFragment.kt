package com.nanoporetech.scainter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@Composable
fun InsuranceInfo(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        CardItem(
            label = stringResource(R.string.policy_status_label),
            value = policyHolder.insuranceStatus,
            valueColor = getStatusColor(policyHolder.insuranceStatus)
        ),
        CardItem(
            label = stringResource(R.string.subscriber_status_label),
            value = policyHolder.providerStatus,
            valueColor = getStatusColor(policyHolder.providerStatus)
        ),
        CardItem(
            stringResource(R.string.policy_type_label),
            policyHolder.insuranceType.uppercase() ?: stringResource(R.string.not_available)
        )
    )

    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium)
        ) {
            CardHeader(
                title = stringResource(R.string.policy_status_title),
                iconImg = Icons.Filled.AttachFile,
                textColor = Color.Black,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBodyTwoLines(
                items = items,
            )
        }
    }
}

@Composable
private fun getStatusColor(status: String): Color {
    return Color.Red
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun InsuranceInfoPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            InsuranceInfo(
                policyHolder =
                    DataSource.policyHolders().first(),
                //DataSource.policyHolders()[1],  // consumption limit reached
                //DataSource.policyHolders()[2],  // status inactive
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}