package com.unlam.recitalapp2.data.models

data class Ticket(
    val id: Long,
    val eventId: Long,
    val quantity: Int,
    val section: String
)
