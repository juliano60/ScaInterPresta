package com.nanoporetech.scainternew.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.data.Datasource
import com.nanoporetech.scainternew.model.Provider
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme
import com.nanoporetech.scainternew.ui.utils.CardHeader
import com.nanoporetech.scainternew.ui.utils.SubHeader

@Composable
fun HealthCareScreen(
    provider: Provider,
    onNewConsultation: () -> Unit,
    onViewConsultations: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_medium)),
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
        ) {
            // MAIN HEADER SECTION
            MainHeader(
                title = provider.displayedName,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // SUB HEADER SECTION
            SubHeader(
                title = stringResource(R.string.dashboard),
                modifier = Modifier
                    .fillMaxWidth()
            )

            // CONSULTATION MENU
            ConsultationCard(
                onNewConsultation = onNewConsultation,
                onViewConsultations = onViewConsultations,
                modifier = Modifier
                    .fillMaxWidth()
            )

            // EXAMINATION MENU
            ExaminationCard(
                modifier = Modifier
                    .fillMaxWidth()
            )

            // HOSPITALISATION MENU
            HospitalisationCard(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MainHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = stringResource(R.string.welcome_header, title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CardRow(
    title: String,
    iconImg: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier) {
        Icon(
            imageVector = iconImg,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_small))
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.horizontal_spacing_small)))
        Text(
            text = title,
            color = Color.White,
        )
    }
}

@Composable
fun ConsultationCard(
    onNewConsultation: () -> Unit,
    onViewConsultations: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = AppConstants.mainGreen
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_small)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
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
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_small)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            CardHeader(
                title = stringResource(R.string.examination_menu_title),
                iconImg = Icons.Outlined.GraphicEq
            )

            CardRow(
                title = stringResource(R.string.new_examination),
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
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vertical_spacing_small)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            CardHeader(
                title = stringResource(R.string.hospitalisation_menu_title),
                iconImg = Icons.Filled.Bed
            )

            CardRow(
                title = stringResource(R.string.new_hospitalisation),
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
    locale = "fr-rCI",
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
                provider = Datasource.healthProviders().first(),
                onNewConsultation = {},
                onViewConsultations = {},
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}