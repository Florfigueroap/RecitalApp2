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
import androidx.cardview.widget.CardView

class EventAdapter(
    private val events: List<Item>,
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.eventImage)
        val txtArtist: TextView = itemView.findViewById(R.id.eventTitle)
        val btnVerMas: Button = itemView.findViewById(R.id.btnBuy)
    }

    // Se llama cuando el RecyclerView necesita un nuevo ViewHolder para representar un elemento.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show, parent, false)
        return EventViewHolder(view)
    }

    // Se llama cuando el RecyclerView necesita establecer los datos del elemento en la vista.
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = events[position]
        // Texto
        holder.txtArtist.text = item.artist

        // Imagen desde drawable
        holder.imgArtist.setImageResource(item.image)

        // Botón
        holder.btnVerMas.setOnClickListener {
            onClick(item)
        }
    }
        // Retorna el tamaño de la lista
        override fun getItemCount() = events.size

}
