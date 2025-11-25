package com.unlam.recitalapp2.data.repositories

import com.unlam.recitalapp2.data.models.Item
import com.unlam.recitalapp2.data.models.TicketWithItem
import java.time.LocalDate

object TicketUiRepository {

    private fun toLocalDate(item: Item): LocalDate =
        LocalDate.parse(item.date) // formato 2025-10-02

    fun getTicketsUpcomingForUser(userId: Long): List<TicketWithItem> {
        val tickets = TicketCollectionRepository.getTicketsForUser(userId)

        val list = mutableListOf<TicketWithItem>()

        for (t in tickets) {
            val item = SimpleItemRepository.getById(t.eventId) ?: continue
            list.add(TicketWithItem(t, item))
        }

        val today = LocalDate.now()

        return list
            .filter { toLocalDate(it.item) >= today }      // futuros o hoy → próximos
            .sortedBy { toLocalDate(it.item) }             // orden ascendente
    }

    fun getTicketsUsedForUser(userId: Long): List<TicketWithItem> {
        val tickets = TicketCollectionRepository.getTicketsForUser(userId)

        val list = mutableListOf<TicketWithItem>()

        for (t in tickets) {
            val item = SimpleItemRepository.getById(t.eventId) ?: continue
            list.add(TicketWithItem(t, item))
        }

        val today = LocalDate.now()

        return list
            .filter { toLocalDate(it.item) < today }       // fechas pasadas → usados
            .sortedByDescending { toLocalDate(it.item) }   // los más recientes primero
    }
}
