package com.unlam.recitalapp2.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.adapter.EventAdapter
import com.unlam.recitalapp2.data.models.Item
import com.unlam.recitalapp2.data.repositories.SimpleItemRepository
import java.time.LocalDate

class ComprarShowActivity : AppCompatActivity() {

    private var userId: Long = -1L

    private lateinit var etArtista: EditText
    private lateinit var rvResultados: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar_show)

        userId = intent.getLongExtra("USER_ID", -1L)

        etArtista = findViewById(R.id.etArtista)
        rvResultados = findViewById(R.id.rvResultadosShows)
        rvResultados.layoutManager = LinearLayoutManager(this)

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnVerTodos = findViewById<Button>(R.id.btnVerTodos)

        btnBuscar.setOnClickListener { buscarPorArtista() }
        btnVerTodos.setOnClickListener { mostrarTodosProximos() }
    }

    private fun obtenerShowsProximos(): List<Item> {
        val hoy = LocalDate.now()
        return SimpleItemRepository.getTodos()
            .filter { LocalDate.parse(it.date) >= hoy }
            .sortedBy { it.date }
    }

    private fun buscarPorArtista() {
        val texto = etArtista.text.toString().trim()
        if (texto.isEmpty()) {
            Toast.makeText(this, "Ingresá el nombre de un artista", Toast.LENGTH_SHORT).show()
            return
        }

        val lista = obtenerShowsProximos()
            .filter { it.artist.contains(texto, ignoreCase = true) }

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay shows próximos de ese artista", Toast.LENGTH_LONG).show()
        } else {
            mostrarEnRecycler(lista)
        }
    }

    private fun mostrarTodosProximos() {
        val lista = obtenerShowsProximos()
        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay shows próximos", Toast.LENGTH_LONG).show()
        } else {
            mostrarEnRecycler(lista)
        }
    }

    private fun mostrarEnRecycler(lista: List<Item>) {
        rvResultados.adapter = EventAdapter(lista) { item ->
            val intent = Intent(this, CompraEntradaActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("EVENT_ID", item.id)
            startActivity(intent)
        }
    }
}
