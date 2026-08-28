package com.nanoporetech.scainter.ui.hospitalisation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.DataSource
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.model.imageUrl
import com.nanoporetech.scainter.ui.components.CardBody
import com.nanoporetech.scainter.ui.components.CardBodyTwoLines
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.CardHeaderDrawable
import com.nanoporetech.scainter.ui.components.CardItem
import com.nanoporetech.scainter.ui.components.PolicyHolderInfoFragment
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.utils.displayedDateAndTime
import com.nanoporetech.scainter.ui.utils.formatCurrency


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalisationDetailsScreen(
    hospitalisation: Hospitalisation,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(paddingMedium),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // SUBSCRIBER INFO
            PolicyHolderInfoFragment(
                name = hospitalisation.fullname,
                internalId = hospitalisation.internalId,
                subscriberName = hospitalisation.subscriberName,
                contractType = hospitalisation.contractType,
                imageUrl = hospitalisation.imageUrl,
                coverPercent = hospitalisation.coverPercentage,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // HOSPITALISATION INFO SECTION
            HospitalisationInfo(
                hospitalisation = hospitalisation,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // COSTS SECTION
            CostsFragment(
                roomCost = hospitalisation.roomCost.toString(),
                hospitalisationCost = hospitalisation.hospitalisationCost.toString(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun HospitalisationInfo(
    hospitalisation: Hospitalisation,
    modifier: Modifier = Modifier
) {
    val items = mutableListOf<CardItem>()

    fun addIfNotEmpty(key: String?, value: String?, valueColor: Color? = null) {
        if (!key.isNullOrBlank() && !value.isNullOrBlank()) {
            items.add(CardItem(label = key, value = value, valueColor = valueColor))
        }
    }

    addIfNotEmpty(stringResource(R.string.hosp_type_label), hospitalisation.type, Color.Red)
    addIfNotEmpty(stringResource(R.string.hosp_status_label), hospitalisation.status, Color.Red)
    addIfNotEmpty(stringResource(R.string.hosp_reason_label), hospitalisation.reason)
    addIfNotEmpty(stringResource(R.string.hosp_num_days_label),
stringResource(R.string.hosp_num_days_res, hospitalisation.durationDays),
        Color.Red)
    addIfNotEmpty(stringResource(R.string.hosp_room_type_label), hospitalisation.roomType)
    addIfNotEmpty(stringResource(R.string.date_label), displayedDateAndTime(hospitalisation.creationDate))

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
            CardHeaderDrawable(
                title = stringResource(R.string.hosp_info_title),
                iconImg = painterResource(R.drawable.cardiology),
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBodyTwoLines(items = items)
        }
    }
}

@Composable
private fun CostsFragment(
    roomCost: String,
    hospitalisationCost: String,
    modifier: Modifier = Modifier
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val items = listOf(
        CardItem(stringResource(R.string.hosp_room_cost_label),
            formatCurrency(roomCost.toDoubleOrNull() ?: 0.0)),
        CardItem(stringResource(R.string.hosp_hospitalisation_cost_label),
            formatCurrency(hospitalisationCost.toDoubleOrNull() ?: 0.0)),
        )

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
                title = stringResource(R.string.total_cost_title),
                iconImg = Icons.Filled.Payments,
                modifier = Modifier
                    .padding(bottom = paddingMedium)
            )

            CardBody(
                items = items,
                indentRight = true
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun HospitalisationDetailsScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                HospitalisationDetailsScreen(
                    hospitalisation = DataSource.hospitalisations()[0],
                    isRefreshing = false,
                )
            }
        }
    }
}