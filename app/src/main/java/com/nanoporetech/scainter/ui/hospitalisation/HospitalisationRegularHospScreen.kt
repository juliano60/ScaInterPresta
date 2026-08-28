package com.nanoporetech.scainter.ui.hospitalisation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.data.HospitalisationUiState
import com.nanoporetech.scainter.ui.components.CardHeader
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimaryOutlinedTextField
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalisationRegularHospScreen(
    state: HospitalisationUiState,
    modifier: Modifier = Modifier,
    onHospitalisationTypeSelected: (String) -> Unit = {},
    onReasonChanged: (String) -> Unit = {},
    onNumDaysChanged: (String) -> Unit = {},
    onRoomTypeSelected: (String) -> Unit = {},
    onRoomCostChanged: (String) -> Unit = {},
    onSubmitRequest: () -> Unit = {}
) {
    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val paddingSmall = dimensionResource(R.dimen.padding_small)
    val focusManager = LocalFocusManager.current
    var expanded by rememberSaveable { mutableStateOf(false) }
    var roomTypeExpanded by rememberSaveable { mutableStateOf(false) }
    val hospTypeFocusRequester = remember { FocusRequester() }
    val reasonFocusRequester = remember { FocusRequester() }
    val numDaysFocusRequester = remember { FocusRequester() }
    val roomTypeFocusRequester = remember { FocusRequester() }
    val roomCostFocusRequester = remember { FocusRequester() }
    val isFormValid = state.selectedHospitalisationType.isNotBlank() &&
            state.reason.isNotBlank() &&
            state.numDays.isNotBlank() &&
            state.selectedRoomType.isNotBlank() &&
            state.roomCost.isNotBlank()

    Column(
        modifier = modifier
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown && event.key == Key.Tab) {
                    focusManager.moveFocus(
                        if (event.isShiftPressed) {
                            FocusDirection.Previous
                        } else {
                            FocusDirection.Next
                        }
                    )
                } else {
                    false
                }
            }
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_small)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingMedium)
            ) {

                CardHeader(
                    title = stringResource(R.string.exam_request_title),
                    iconImg = Icons.Filled.EditNote,
                    modifier = Modifier
                        .padding(bottom = paddingMedium)
                )

                // TYPES
                Text(
                    text = stringResource(R.string.hosp_type_label),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                ) {
                    OutlinedTextField(
                        value = state.selectedHospitalisationType,
                        readOnly = true,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.hosp_type_hint)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .focusRequester(hospTypeFocusRequester)
                            .menuAnchor(
                                MenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        state.hospOptions.forEach { optionRes ->
                            val option = stringResource(optionRes)

                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick =  {
                                    onHospitalisationTypeSelected(option)
                                    expanded = false
                                    reasonFocusRequester.requestFocus()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // REASON
                Text(
                    text = stringResource(R.string.hosp_reason_label),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PrimaryOutlinedTextField(
                    value = state.reason,
                    placeholder = stringResource(R.string.hosp_reason_hint),
                    onValueChanged = onReasonChanged,
                    modifier = Modifier
                        .focusRequester(reasonFocusRequester)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { numDaysFocusRequester.requestFocus() }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // NUMBER OF DAYS
                Text(
                    text = stringResource(R.string.hosp_num_days_label),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PrimaryOutlinedTextField(
                    value = state.numDays,
                    placeholder = stringResource(R.string.hosp_num_days_hint),
                    onValueChanged = { value ->
                        if (value.all { it.isDigit() }) {
                            onNumDaysChanged(value)
                        }
                    },
                    modifier = Modifier
                        .focusRequester(numDaysFocusRequester)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { roomTypeFocusRequester.requestFocus() }
                    ),
                )

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // ROOM TYPES
                Text(
                    text = stringResource(R.string.hosp_room_type_label),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                ExposedDropdownMenuBox(
                    expanded = roomTypeExpanded,
                    onExpandedChange = {
                        roomTypeExpanded = !roomTypeExpanded
                    },
                ) {
                    OutlinedTextField(
                        value = state.selectedRoomType,
                        readOnly = true,
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(R.string.hosp_room_type_hint)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = roomTypeExpanded
                            )
                        },
                        modifier = Modifier
                            .focusRequester(roomTypeFocusRequester)
                            .menuAnchor(
                                MenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = roomTypeExpanded,
                        onDismissRequest = { roomTypeExpanded = false }
                    ) {
                        state.roomOptions.forEach { optionRes ->
                            val option = stringResource(optionRes)

                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick =  {
                                    onRoomTypeSelected(option)
                                    roomCostFocusRequester.requestFocus()
                                    roomTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(paddingMedium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(paddingMedium))

                // ROOM COST
                Text(
                    text = stringResource(R.string.hosp_room_cost_label),
                    color = AppConstants.mainGreen,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(paddingSmall))

                PrimaryOutlinedTextField(
                    value = state.roomCost,
                    placeholder = stringResource(R.string.hosp_room_cost_hint),
                    onValueChanged = { value ->
                        if (value.all { it.isDigit() }) {
                            onRoomCostChanged(value)
                        }
                    },
                    modifier = Modifier
                        .focusRequester(roomCostFocusRequester)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (isFormValid) {
                                onSubmitRequest()
                            }
                        }
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(paddingMedium))

        // SEND BUTTON
        PrimaryButton(
            iconImg = Icons.AutoMirrored.Filled.Send,
            text = stringResource(R.string.send_button),
            onClick = onSubmitRequest,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(
    locale = "fr-rCI",
    showBackground = true,
)
@Composable
fun HospitalisationRegularHospScreenPreview() {
    ScaInterAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HospitalisationRegularHospScreen(
                state = HospitalisationUiState(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppConstants.lightGreen)
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}