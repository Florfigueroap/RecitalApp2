package com.unlam.recitalapp2.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.adapter.EventAdapter
import com.unlam.recitalapp2.data.repositories.SimpleItemRepository

class MainActivity : AppCompatActivity() {

    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        userId = intent.getLongExtra("USER_ID", -1L)

        setupBottomNavigation()
        setupButtonsHome()
        setupRecyclerShows()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Botones comprar / próximos shows
    private fun setupButtonsHome() {
        val btnComprar = findViewById<Button>(R.id.btnComprar)
        val btnProximos = findViewById<Button>(R.id.btnProximosShows)

        btnComprar.setOnClickListener {
            val intent = Intent(this, ComprarShowActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnProximos.setOnClickListener {
            val intent = Intent(this, ProximosShowsActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    // Lista de shows en el home
    private fun setupRecyclerShows() {
        val rv = findViewById<RecyclerView>(R.id.rvShows)
        rv.layoutManager = LinearLayoutManager(this)

        val shows = SimpleItemRepository.getTodos()
        rv.adapter = EventAdapter(shows) { item ->
            val intent = Intent(this, CompraEntradaActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("EVENT_ID", item.id)
            startActivity(intent)
        }
    }


    // Navegación inferior
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_main

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Perfil
                R.id.navigation_screen1 -> {
                    val intent = Intent(this, PerfilUser::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                // Home
                R.id.navigation_main -> true

                // Tickets
                R.id.navigation_screen2 -> {
                    val intent = Intent(this, HistorialTickets::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
