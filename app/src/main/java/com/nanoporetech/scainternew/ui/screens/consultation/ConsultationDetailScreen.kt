package com.nanoporetech.scainternew.screens.consultation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.data.Datasource
import com.nanoporetech.scainternew.model.Consultation
import com.nanoporetech.scainternew.model.imageUrl
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme
import com.nanoporetech.scainternew.ui.utils.CardHeader
import com.nanoporetech.scainternew.utils.displayedDate
import com.nanoporetech.scainternew.utils.displayedDateAndTime

@Composable
fun ConsultationDetailScreen(
    consultation: Consultation,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        // NAME AND ICON
        AssureInfo(
            fullname = consultation.fullname,
            subscriberName = consultation.subscriberName,
            contractType = consultation.contractType,
            dateOfBirth = displayedDate(consultation.dateOfBirth),
            internalId = consultation.internalId,
            imageUrl = consultation.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
        )

        // CONSULTATION INFO
        ConsultationInfo(
            act = consultation.act,
            date = displayedDateAndTime(consultation.creationDate),
            coverage = consultation.percentageCoverage,
            modifier = Modifier
                .fillMaxWidth()
        )

        // COSTS INFO
        CostInfo(
            total = "${consultation.total}",
            totalSca = "${consultation.totalSca}",
            totalHolder = "${consultation.totalUser}",
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
fun AssureInfo(
    fullname: String,
    subscriberName: String,
    contractType: String,
    dateOfBirth: String,
    internalId: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ProfileImage(
            imageUrl = imageUrl,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text(
            text = fullname,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = internalId,
            color = Color.Red,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = dateOfBirth,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$subscriberName ($contractType)",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ProfileImage(
    imageUrl: String,
    modifier: Modifier
) {
    if (imageUrl.isNotBlank()) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(dimensionResource(R.dimen.profile_icon_size))
                .shadow(dimensionResource(R.dimen.shadow_size), CircleShape)
                .clip(CircleShape),
            loading = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.profile_image),
                    tint = AppConstants.mainGreen,
                    modifier = Modifier.matchParentSize()
                )
            },
            error = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.profile_image),
                    tint = AppConstants.mainGreen,
                    modifier = Modifier.matchParentSize()
                )
            },
        )
    } else {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = stringResource(R.string.profile_image),
            modifier = modifier
                .size(dimensionResource(R.dimen.profile_icon_size))
        )
    }
}

@Composable
fun ConsultationInfo(
    act: String,
    date: String,
    coverage: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            CardHeader(
                title = stringResource(R.string.consultation_info_title),
                iconImg = Icons.Filled.MedicalServices,
                color = AppConstants.mainGreen
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            Row(

            ) {
                Text(
                    text = stringResource(R.string.act_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = act,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }

            Row(

            ) {
                Text(
                    text = stringResource(R.string.date_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = date,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }

            Row(

            ) {
                Text(
                    text = stringResource(R.string.coverage_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = coverage,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }
        }
    }
}


@Composable
fun CostInfo(
    total: String,
    totalSca: String,
    totalHolder: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            CardHeader(
                title = stringResource(R.string.total_cost_title),
                iconImg = Icons.Filled.AddCircle,
                color = AppConstants.mainGreen
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            Row(

            ) {
                Text(
                    text = stringResource(R.string.total_cost_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = total,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }

            Row(

            ) {
                Text(
                    text = stringResource(R.string.total_sca_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = totalSca,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }

            Row(

            ) {
                Text(
                    text = stringResource(R.string.total_holder_label),
                    modifier = Modifier
                        .weight(0.7f)
                )
                Text(
                    text = totalHolder,
                    modifier = Modifier
                        .weight(1.0f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ConsultationDetailScreenPreview() {
    ScaInterNewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ConsultationDetailScreen(
                consultation = Datasource.consultations().first(),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}