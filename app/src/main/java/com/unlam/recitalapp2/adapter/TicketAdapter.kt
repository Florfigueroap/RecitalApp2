package com.unlam.recitalapp2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.TicketWithItem

class TicketAdapter(
    private val tickets: List<TicketWithItem>
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtArtist: TextView = itemView.findViewById(R.id.txtTicketArtist)
        val txtInfo: TextView = itemView.findViewById(R.id.txtTicketInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val twi = tickets[position]
        val event = twi.item
        val ticket = twi.ticket

        holder.txtArtist.text = event.artist
        holder.txtInfo.text =
            "${event.date} - ${event.hour} • ${event.place} • ${ticket.section} x${ticket.quantity}"
    }

    override fun getItemCount(): Int = tickets.size
}
