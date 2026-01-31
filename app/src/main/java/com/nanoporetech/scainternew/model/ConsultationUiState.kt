package com.nanoporetech.scainternew.model

data class ConsultationUiState(
    // barScanState is the QR code scanner state
    val barScanState: BarScanState = BarScanState.Idle,
    // familyMembers is the list of family members
    val familyMembers: List<FamilyMember> = emptyList()
)