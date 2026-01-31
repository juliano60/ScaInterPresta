package com.nanoporetech.scainternew.screens.hospitalisation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.data.Datasource
import com.nanoporetech.scainternew.model.Hospitalisation
import com.nanoporetech.scainternew.utils.displayedDateAndTime


@Composable
fun HospitalisationListView(
    modifier: Modifier = Modifier,
    hospitalisations: List<Hospitalisation> = Datasource.hospitalisations()
) {
    Column(modifier =
    Modifier.fillMaxSize()
    ) {
        if (hospitalisations.isEmpty()) {
            Text(text = "noRecentHospitalisation")
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .safeContentPadding()
                    .statusBarsPadding()
            ) {
                items(hospitalisations) { hospitalisation ->
                    HospitalisationRowItem(
                        hospitalisation = hospitalisation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun HospitalisationRowItem(
    hospitalisation: Hospitalisation,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null)

            Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

            Text(
                text = hospitalisation.fullname,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = hospitalisation.type,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = displayedDateAndTime(hospitalisation.creationDate),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun HospitalisationListPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //HospitalisationListView()
    }
}