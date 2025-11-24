package com.unlam.recitalapp2.data.repositories

import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.Item

object SimpleItemRepository {

    private val events = mutableListOf<Item>()

    init {
        events.add(
            Item(
                1L,
                "2025-10-02",
                "21:00",
                "Luna Park",
                "Abel Pintos",
                R.drawable.abelpintos,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                2L,
                "2025-12-29",
                "20:00",
                "Estadio River Plate",
                "Duki",
                R.drawable.duki,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                3L,
                "2021-07-30",
                "22:00",
                "Estadio Velez Sarsfield",
                "Fito Paez",
                R.drawable.fito,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                4L,
                "2025-11-16",
                "20:00",
                "Teatro Gran Rex",
                "Tini",
                R.drawable.tini,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                5L,
                "2025-09-21",
                "19:00",
                "Movistar Arena",
                "La Renga",
                R.drawable.larenga,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                6L,
                "2025-11-09",
                "21:00",
                "Hipodromo de Palermo",
                "Bizarrap",
                R.drawable.bzrp,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )

        events.add(
            Item(
                7L,
                "2025-07-18",
                "20:00",
                "Teatro Vorterix",
                "Skrillex",
                R.drawable.skrillex,
                capacityCampo = 50,
                capacityPlatea = 50
            )
        )
    }

    fun getPorArtista(artista: String): List<Item> {
        val evento = mutableListOf<Item>()
        val mayusculas = artista.uppercase()
        for (e in events) {
            if (e.artist.uppercase() == mayusculas) {
                evento.add(e)
            }
        }
        return evento
    }

    fun getById(id: Long): Item? {
        return events.find { it.id == id }
    }

    fun getTodos(): List<Item> {
        return events.toList()
    }
}