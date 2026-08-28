package com.nanoporetech.scainter.model

import android.net.Uri
import com.nanoporetech.scainter.conf.AppConfiguration
import com.nanoporetech.scainter.conf.appConfig
import kotlinx.serialization.Serializable

@Serializable
data class Hospitalisation(
    val id: Int,
    val fullname: String,
    val internalId: String,
    val coverPercentage: String,
    val subscriberName: String,
    val contractType: String,
    val type: String,
    val status: String,
    val reason: String,
    val durationDays: Int,
    val roomType: String,
    val prolongationReason: String,
    val prolongationDays: Int,
    val providerName: String,
    val creationDate: String,
    val roomCost: Double,
    val hospitalisationCost: Double,
    val dateOfBirth: String
) {
}

val Hospitalisation.imageUrl: String
    get() {
        return "${appConfig.httpProtocol}://${appConfig.hostname}${appConfig.imagesPath}/$internalId.jpg"
    }