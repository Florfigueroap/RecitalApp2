package com.unlam.recitalapp2.data.models

import java.io.Serializable

data class Item(
    val id: Long,
    val date: String,
    val hour: String,
    val place: String,
    val artist: String,
    val image: Int,
    val capacityCampo: Int,
    val capacityPlatea: Int
): Serializable
