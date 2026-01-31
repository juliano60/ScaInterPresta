package com.nanoporetech.scainternew.ui.screens.consultation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainternew.R
import com.nanoporetech.scainternew.conf.AppConstants
import com.nanoporetech.scainternew.data.Datasource
import com.nanoporetech.scainternew.model.FamilyMember
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme
import com.nanoporetech.scainternew.ui.utils.SubHeader

@Composable
fun FamilyMembersConsListScreen(
    members: List<FamilyMember>,
    onMemberSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Column(

        ) {
            // SUB HEADER SECTION
            SubHeader(
                title = stringResource(R.string.policy_members),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // FAMILY MEMBERS
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(members) { member ->
                    MemberRowItem(
                        member = member,
                        onMemberSelected = onMemberSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceDim
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MemberRowItem(
    member: FamilyMember,
    modifier: Modifier = Modifier,
    onMemberSelected: () -> Unit = {},
) {
    Card(
        shape = RectangleShape,
        onClick = onMemberSelected,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

                    Text(
                        text = member.fullname,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = member.link,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .weight(0.1f)
            )
        }
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun FamilyMembersConsListScreenPreview() {
    ScaInterNewTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FamilyMembersConsListScreen(
                members = Datasource.policyHolderFamilyMembers(),
                onMemberSelected = {},
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}