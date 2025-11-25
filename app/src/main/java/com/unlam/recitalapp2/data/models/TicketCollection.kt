package com.unlam.recitalapp2.data.models

data class TicketCollection(
    val id: Long,
    val userId: Long,
    val ticketCollection: MutableList<Long>
)