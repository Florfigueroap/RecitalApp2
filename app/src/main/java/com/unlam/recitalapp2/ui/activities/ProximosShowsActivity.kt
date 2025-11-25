package com.unlam.recitalapp2.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.adapter.EventAdapter
import com.unlam.recitalapp2.data.models.Item
import com.unlam.recitalapp2.data.repositories.SimpleItemRepository
import java.time.LocalDate

class ProximosShowsActivity : AppCompatActivity() {

    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proximos_shows)

        userId = intent.getLongExtra("USER_ID", -1L)

        val rv = findViewById<RecyclerView>(R.id.rvProximosShows)
        rv.layoutManager = LinearLayoutManager(this)

        val lista = obtenerShowsProximos()
        rv.adapter = EventAdapter(lista) { item ->
            val intent = Intent(this, CompraEntradaActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("EVENT_ID", item.id)
            startActivity(intent)
        }
    }

    private fun obtenerShowsProximos(): List<Item> {
        val hoy = LocalDate.now()
        return SimpleItemRepository.getTodos()
            .filter { LocalDate.parse(it.date) >= hoy }
            .sortedBy { it.date }
    }
}
