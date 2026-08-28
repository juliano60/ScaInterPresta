package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.model.ExamOption

data class HospitalisationUiState(
    /** the selected hospitalisation type */
    val selectedHospitalisationType: String = "",
    /** the list of hospitalisation types */
    val hospOptions: List<Int> = listOf(
        R.string.hosp_placement_under_observation,
        R.string.hosp_medical,
        R.string.hosp_surgical,
        R.string.hosp_child_birth,
        R.string.hosp_other
    ),
    /** the reason for hospitalisation */
    val reason: String = "",
    /** the number of days requested */
    val numDays: String = "",
    /** the selected room type (Simple, Double, Suite ou Autre) */
    val selectedRoomType: String = "",
    /** the list of room types */
    val roomOptions: List<Int> = listOf(
        R.string.hosp_room_single,
        R.string.hosp_room_double,
        R.string.hosp_room_suite,
        R.string.hosp_room_other,
    ),
    /** the room cost */
    val roomCost: String = "",
    /** the request is being submitted */
    val isSubmitting: Boolean = false
)
