package com.unlam.recitalapp2.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.adapter.TicketAdapter
import com.unlam.recitalapp2.data.repositories.TicketUiRepository

class HistorialTickets : AppCompatActivity() {

    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_tickets)

        userId = intent.getLongExtra("USER_ID", -1L)

        setupTicketLists()
    }

    private fun setupTicketLists() {
        val rvUpcoming = findViewById<RecyclerView>(R.id.rvUpcomingTickets)
        val rvUsed = findViewById<RecyclerView>(R.id.rvUsedTickets)

        rvUpcoming.layoutManager = LinearLayoutManager(this)
        rvUsed.layoutManager = LinearLayoutManager(this)

        val upcomingTickets = TicketUiRepository.getTicketsUpcomingForUser(userId)
        val usedTickets = TicketUiRepository.getTicketsUsedForUser(userId)

        rvUpcoming.adapter = TicketAdapter(upcomingTickets)
        rvUsed.adapter = TicketAdapter(usedTickets)
    }
}

