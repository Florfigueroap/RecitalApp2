package com.unlam.recitalapp2.data.repositories

import com.unlam.recitalapp2.data.models.Ticket
import com.unlam.recitalapp2.data.models.TicketCollection

object TicketCollectionRepository {

    private val ticketCollections = mutableListOf<TicketCollection>()

    init {
        ticketCollections.add(
            TicketCollection(
                1L,
                1510L,
                mutableListOf(1L, 3L, 12L, 27L, 5L, 19L, 8L, 30L, 2L, 14L, 22L, 9L)
            )
        )
        ticketCollections.add(
            TicketCollection(
                2L,
                1504L,
                mutableListOf(1L, 3L, 6L, 17L, 30L, 11L, 24L, 3L, 29L, 18L, 6L, 10L)
            )
        )
        ticketCollections.add(
            TicketCollection(
                3L,
                2802L,
                mutableListOf(1L, 3L, 25L, 7L, 14L, 30L, 2L, 12L, 28L, 19L, 5L, 25L)
            )
        )
    }

    fun get(): List<TicketCollection> = ticketCollections

    fun getPorIdUsuario(userId: Long): TicketCollection? {
        for (i in ticketCollections) {
            if (i.userId == userId) return i
        }
        return null
    }

    fun addTicketAUsuario(userId: Long, ticketId: Long): Boolean {
        val coleccion = getPorIdUsuario(userId) ?: return false
        if (coleccion.ticketCollection.contains(ticketId)) {
            return false
        } else {
            coleccion.ticketCollection.add(ticketId)
            return true
        }
    }

    fun getTicketsForUser(userId: Long): List<Ticket> {
        val coleccion = getPorIdUsuario(userId) ?: return emptyList()
        return coleccion.ticketCollection.mapNotNull { id ->
            TicketsRepository.getById(id)
        }
    }
}
