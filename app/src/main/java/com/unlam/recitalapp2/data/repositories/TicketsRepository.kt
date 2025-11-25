package com.unlam.recitalapp2.data.repositories

import com.unlam.recitalapp2.data.models.Ticket

object TicketsRepository {

    private val tickets = mutableListOf<Ticket>()

    init {
        tickets.add(Ticket(1L, 4L, 1, "Platea"))
        tickets.add(Ticket(2L, 3L, 4, "Campo"))
        tickets.add(Ticket(3L, 1L, 2, "Campo"))
        tickets.add(Ticket(4L, 5L, 6, "Platea"))
        tickets.add(Ticket(5L, 2L, 3, "Platea"))
        tickets.add(Ticket(6L, 1L, 7, "Campo"))
        tickets.add(Ticket(7L, 3L, 4, "Platea"))
        tickets.add(Ticket(8L, 1L, 1, "Campo"))
        tickets.add(Ticket(9L, 7L, 2, "Campo"))
        tickets.add(Ticket(10L, 7L, 4, "Platea"))
        tickets.add(Ticket(11L, 5L, 7, "Campo"))
        tickets.add(Ticket(12L, 2L, 2, "Platea"))
        tickets.add(Ticket(13L, 4L, 4, "Platea"))
        tickets.add(Ticket(14L, 6L, 2, "Platea"))
        tickets.add(Ticket(15L, 2L, 7, "Campo"))
        tickets.add(Ticket(16L, 5L, 4, "Campo"))
        tickets.add(Ticket(17L, 3L, 1, "Platea"))
        tickets.add(Ticket(18L, 1L, 3, "Platea"))
        tickets.add(Ticket(19L, 3L, 4, "Platea"))
        tickets.add(Ticket(20L, 1L, 5, "Campo"))
        tickets.add(Ticket(21L, 5L, 6, "Platea"))
        tickets.add(Ticket(22L, 4L, 2, "Platea"))
        tickets.add(Ticket(23L, 6L, 1, "Campo"))
        tickets.add(Ticket(24L, 3L, 4, "Platea"))
        tickets.add(Ticket(25L, 2L, 3, "Platea"))
        tickets.add(Ticket(26L, 7L, 8, "Platea"))
        tickets.add(Ticket(27L, 7L, 1, "Campo"))
        tickets.add(Ticket(28L, 1L, 4, "Campo"))
        tickets.add(Ticket(29L, 2L, 2, "Campo"))
        tickets.add(Ticket(30L, 5L, 3, "Platea"))
    }

    fun add(eventId: Long, quantity: Int, section: String): Ticket {
        var maxId = 0L
        for (i in tickets) if (i.id > maxId) maxId = i.id
        val sigtId = maxId + 1L
        val tkt = Ticket(sigtId, eventId, quantity, section)
        tickets.add(tkt)
        return tkt
    }

    fun getById(id: Long): Ticket? {
        return tickets.find { it.id == id }
    }

    fun vendidos(eventId: Long, section: String): Int {
        var total = 0
        for (t in tickets) {
            if (t.eventId == eventId && t.section.equals(section, ignoreCase = true)) {
                total += t.quantity
            }
        }
        return total
    }

    fun capacidadDisponible(eventId: Long, section: String, capCampo: Int, capPlatea: Int): Int {
        val capacidad = if (section.equals("Campo", ignoreCase = true)) capCampo else capPlatea
        return capacidad - vendidos(eventId, section)
    }
}
