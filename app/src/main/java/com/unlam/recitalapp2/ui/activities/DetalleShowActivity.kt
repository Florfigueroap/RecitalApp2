package com.unlam.recitalapp2.ui.activities
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.unlam.recitalapp2.data.models.Item
import com.unlam.recitalapp2.R

class DetalleShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_show)

        // Referencias a la UI
        val imgArtist = findViewById<ImageView>(R.id.imgDetalle)
        val txtArtist = findViewById<TextView>(R.id.txtDetalleArtist)
        val txtDate = findViewById<TextView>(R.id.txtDetalleFecha)
        val txtPlace = findViewById<TextView>(R.id.txtDetalleLugar)
        val btnComprar = findViewById<Button>(R.id.btnComprar)

        // Recibir el item
        val item = intent.getSerializableExtra("item") as Item

        // Mostrar datos
        imgArtist.setImageResource(item.image)
        txtArtist.text = item.artist
        txtDate.text = "Fecha: ${item.date} - ${item.hour}"
        txtPlace.text = "Lugar: ${item.place}"

        btnComprar.setOnClickListener {
            // Acá hacés lo que quieras después
        }
    }
}