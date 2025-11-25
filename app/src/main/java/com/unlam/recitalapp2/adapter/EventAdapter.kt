package com.unlam.recitalapp2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.Item
import java.time.LocalDate

class EventAdapter(
    private val events: List<Item>,
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.eventImage)
        val txtArtist: TextView = itemView.findViewById(R.id.eventTitle)
        val btnVerMas: Button = itemView.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = events[position]

        holder.txtArtist.text = item.artist
        holder.imgArtist.setImageResource(item.image)

        val isPastShow = try {
            val showDate = LocalDate.parse(item.date)   // "2025-12-29"
            val today = LocalDate.now()
            showDate.isBefore(today)
        } catch (e: Exception) {
            false
        }

        val ctx = holder.itemView.context

        if (isPastShow) {
            holder.btnVerMas.isEnabled = false
            holder.btnVerMas.text = "Sold out"
            holder.btnVerMas.alpha = 0.7f

            holder.btnVerMas.setTextColor(ctx.getColor(R.color.white))

            holder.btnVerMas.setOnClickListener(null)
        } else {
            holder.btnVerMas.isEnabled = true
            holder.btnVerMas.text = "Comprar tickets"
            holder.btnVerMas.alpha = 1f

            holder.btnVerMas.setTextColor(ctx.getColor(R.color.white))

            holder.btnVerMas.setOnClickListener {
                onClick(item)
            }
        }
    }


    override fun getItemCount() = events.size
}
