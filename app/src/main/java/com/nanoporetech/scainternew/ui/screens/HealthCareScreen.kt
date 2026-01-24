package com.nanoporetech.scainternew.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme
import com.nanoporetech.scainternew.utils.CardHeader

@Composable
fun HealthCareScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
            .fillMaxSize()
    ) {
        MainHeader(
            modifier = Modifier
                .fillMaxWidth()
        )

        SubHeader()

        ConsultationCard(
            modifier = Modifier
                .fillMaxWidth()
        )

        ExaminationCard(
            modifier = Modifier
                .fillMaxWidth()
        )

        HospitalisationCard(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun MainHeader(
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = stringResource(R.string.welcome_header, "Centre d Ophtalmologie de Kami"),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SubHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.dashboard),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun CardRow(
    title: String,
    iconImg: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier) {
        Icon(
            imageVector = iconImg,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_small))
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun ConsultationCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = AppConstants.mainGreen
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            CardHeader(
                title = stringResource(R.string.consultation_menu_title),
                iconImg = Icons.Outlined.MonitorHeart
            )

            CardRow(
                title = stringResource(R.string.new_care_sheet),
                iconImg = Icons.Outlined.Search
            )

            CardRow(
                title = stringResource(R.string.view_care_sheet),
                iconImg = Icons.AutoMirrored.Outlined.Assignment
            )
        }
    }
}

@Composable
fun ExaminationCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = AppConstants.mainGreen
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            CardHeader(
                title = stringResource(R.string.examination_menu_title),
                iconImg = Icons.Outlined.GraphicEq
            )

            CardRow(
                title = stringResource(R.string.new_care_sheet),
                iconImg = Icons.Outlined.Search
            )

            CardRow(
                title = stringResource(R.string.view_examinations),
                iconImg = Icons.AutoMirrored.Outlined.Assignment
            )
        }
    }
}

@Composable
fun HospitalisationCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = AppConstants.mainGreen
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            CardHeader(
                title = stringResource(R.string.hospitalisation_menu_title),
                iconImg = Icons.Filled.Bed
            )

            CardRow(
                title = stringResource(R.string.new_care_sheet),
                iconImg = Icons.Outlined.Search
            )

            CardRow(
                title = stringResource(R.string.view_hospitalisations),
                iconImg = Icons.AutoMirrored.Outlined.Assignment
            )
        }
    }
}

@Preview(
    //locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun HealthCareScreenPreview() {
    ScaInterNewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HealthCareScreen(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}