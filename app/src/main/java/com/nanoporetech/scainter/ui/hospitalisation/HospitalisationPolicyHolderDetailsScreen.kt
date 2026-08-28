package com.nanoporetech.scainter.ui.hospitalisation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.InsuranceInfo
import com.nanoporetech.scainter.ui.components.OptionCard
import com.nanoporetech.scainter.ui.components.PolicyHolderInfoFragment
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme

@Composable
fun HospitalisationPolicyHolderDetailsScreen(
    policyHolder: PolicyHolder,
    modifier: Modifier = Modifier,
    onNewHospitalisation: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        // SUBSCRIBER INFO
        PolicyHolderInfoFragment(
            name = policyHolder.fullname,
            internalId = policyHolder.internalId,
            subscriberName = policyHolder.subscriberName,
            contractType = policyHolder.contractType,
            imageUrl = policyHolder.imageUrl,
            coverPercent = policyHolder.coverExternal,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // INSURANCE INFO
        InsuranceInfo(
            policyHolder = policyHolder,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // OPTIONS
        OptionsInfo(
            onNewHospitalisation = onNewHospitalisation,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

    }
}

@Composable
private fun OptionsInfo(
    modifier: Modifier = Modifier,
    onNewHospitalisation: () -> Unit,
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)

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
                title = stringResource(R.string.options_title),
                iconImg = Icons.Filled.MedicalInformation,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(paddingSmall),
            ) {
                OptionCard(
                    iconImg = null,
                    title = stringResource(R.string.add_hospitalisation_button),
                    onClick = onNewHospitalisation,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun HospitalisationPolicyHolderDetailsScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HospitalisationPolicyHolderDetailsScreen(
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
