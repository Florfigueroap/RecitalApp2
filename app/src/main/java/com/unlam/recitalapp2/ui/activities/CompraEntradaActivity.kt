package com.unlam.recitalapp2.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.unlam.recitalapp2.R
import com.unlam.recitalapp2.data.models.Item
import com.unlam.recitalapp2.data.models.User
import com.unlam.recitalapp2.data.repositories.SimpleItemRepository
import com.unlam.recitalapp2.data.repositories.TicketCollectionRepository
import com.unlam.recitalapp2.data.repositories.TicketsRepository
import com.unlam.recitalapp2.data.repositories.UserRepository
import com.unlam.recitalapp2.domain.MasterCard
import com.unlam.recitalapp2.domain.MercadoPago
import com.unlam.recitalapp2.domain.MetodoPago
import com.unlam.recitalapp2.domain.MetodoPagoOpcion
import com.unlam.recitalapp2.domain.Visa
import java.time.LocalDateTime

class CompraEntradaActivity : AppCompatActivity() {

    private var userId: Long = -1L
    private var eventId: Long = -1L

    private lateinit var tvTitulo: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvPrecioUnitario: TextView
    private lateinit var tvInfoMetodoPago: TextView
    private lateinit var tvTotalEstimado: TextView

    private lateinit var spSector: Spinner
    private lateinit var etCantidad: EditText
    private lateinit var spMetodoPago: Spinner
    private lateinit var btnConfirmar: Button

    private var item: Item? = null
    private var user: User? = null

    private val precioPorEntrada = 10000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra_entrada)

        userId = intent.getLongExtra("USER_ID", -1L)
        eventId = intent.getLongExtra("EVENT_ID", -1L)

        item = SimpleItemRepository.getById(eventId)
        user = UserRepository.getById(userId)

        if (item == null || user == null) {
            Toast.makeText(this, "Error cargando datos para la compra", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Referencias
        tvTitulo = findViewById(R.id.tvCompraTitulo)
        tvInfo = findViewById(R.id.tvCompraInfo)
        tvPrecioUnitario = findViewById(R.id.tvPrecioUnitario)
        tvInfoMetodoPago = findViewById(R.id.tvInfoMetodoPago)
        tvTotalEstimado = findViewById(R.id.tvTotalEstimado)

        spSector = findViewById(R.id.spSector)
        etCantidad = findViewById(R.id.etCantidad)
        spMetodoPago = findViewById(R.id.spMetodoPago)
        btnConfirmar = findViewById(R.id.btnConfirmarCompra)

        val e = item!!
        tvTitulo.text = e.artist
        tvInfo.text = "${e.date} - ${e.hour} • ${e.place}"

        // Muestra precio por entrada
        tvPrecioUnitario.text = "Precio por entrada: $${precioPorEntrada.toInt()}"

        // Sectores permitidos
        val sectores = listOf("Campo", "Platea")
        spSector.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sectores
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Métodos de pago
        val metodos = MetodoPagoOpcion.values().toList()
        spMetodoPago.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            metodos
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Listener para actualizar descripción y total cuando cambia el metodo de pago
        spMetodoPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                actualizarDescripcionMetodoPago()
                actualizarResumen()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Listener para actualizar total cuando cambia la cantidad
        etCantidad.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                actualizarResumen()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnConfirmar.setOnClickListener {
            confirmarCompra()
        }

        // Inicializar descripción y resumen
        actualizarDescripcionMetodoPago()
        actualizarResumen()
    }

    private fun obtenerMetodoPagoSeleccionado(): MetodoPagoOpcion? {
        return spMetodoPago.selectedItem as? MetodoPagoOpcion
    }

    private fun construirMetodoPago(opcion: MetodoPagoOpcion): MetodoPago {
        return when (opcion) {
            MetodoPagoOpcion.MERCADO_PAGO -> MercadoPago
            MetodoPagoOpcion.VISA -> Visa
            MetodoPagoOpcion.MASTERCARD -> MasterCard
        }
    }

    private fun actualizarDescripcionMetodoPago() {
        val opcion = obtenerMetodoPagoSeleccionado() ?: return
        val texto = when (opcion) {
            MetodoPagoOpcion.MERCADO_PAGO ->
                "Mercado Pago: recargo fijo del 2% sobre el total."
            MetodoPagoOpcion.VISA ->
                "Visa: +1% entre 15:00 y 22:30. Fuera de ese horario, +3%."
            MetodoPagoOpcion.MASTERCARD ->
                "MasterCard: fines de semana +3%. Lunes a viernes +0,75%."
        }
        tvInfoMetodoPago.text = texto
    }

    private fun actualizarResumen() {
        val u = user ?: return
        val opcion = obtenerMetodoPagoSeleccionado() ?: return

        val cantidadStr = etCantidad.text.toString().trim()
        val cantidad = cantidadStr.toIntOrNull()

        if (cantidad == null || cantidad <= 0) {
            tvTotalEstimado.text = ""
            return
        }

        val montoBase = precioPorEntrada * cantidad

        val metodoPago = construirMetodoPago(opcion)

        val ahora = LocalDateTime.now()
        val hora = ahora.hour
        val minuto = ahora.minute
        val diaSemana = ahora.dayOfWeek.value  // 1=lun ... 7=dom

        val total = metodoPago.total(montoBase, hora, minuto, diaSemana)

        val saldo = u.money

        val textoResumen = buildString {
            append("Monto base: $${montoBase.toInt()}  |  Total con recargo: $${total.toInt()}")
            append("\nTu saldo actual: $${saldo.toInt()}")
        }

        tvTotalEstimado.text = textoResumen
    }

    private fun confirmarCompra() {
        val e = item!!
        val u = user!!

        val sector = spSector.selectedItem as String
        val cantidadStr = etCantidad.text.toString().trim()

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingresá una cantidad", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a cero", Toast.LENGTH_SHORT).show()
            return
        }

        // Capacidad disponible según TicketsRepository
        val capacidadDisponible = TicketsRepository.capacidadDisponible(
            eventId = e.id,
            section = sector,
            capCampo = e.capacityCampo,
            capPlatea = e.capacityPlatea
        )

        if (cantidad > capacidadDisponible) {
            Toast.makeText(
                this,
                "No hay capacidad suficiente en $sector. Disponible: $capacidadDisponible",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val montoBase = precioPorEntrada * cantidad

        val opcion = obtenerMetodoPagoSeleccionado()
        if (opcion == null) {
            Toast.makeText(this, "Seleccioná un medio de pago", Toast.LENGTH_SHORT).show()
            return
        }

        val metodoPago: MetodoPago = construirMetodoPago(opcion)

        val ahora = LocalDateTime.now()
        val hora = ahora.hour
        val minuto = ahora.minute
        val diaSemana = ahora.dayOfWeek.value

        val total = metodoPago.total(montoBase, hora, minuto, diaSemana)

        if (u.money < total) {
            Toast.makeText(
                this,
                "Saldo insuficiente. Necesitás $${total.toInt()} y tenés $${u.money.toInt()}",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Descontar saldo
        val actualizado = u.copy(money = u.money - total)
        val okUser = UserRepository.updateUser(this, actualizado)
        if (!okUser) {
            Toast.makeText(this, "No se pudo actualizar el usuario", Toast.LENGTH_SHORT).show()
            return
        }
        user = actualizado

        // Generar ticket
        val ticket = TicketsRepository.add(
            eventId = e.id,
            quantity = cantidad,
            section = sector
        )
        // Asociar ticket a este usuario
        TicketCollectionRepository.addTicketAUsuario(userId, ticket.id)

        Toast.makeText(
            this,
            "Compra realizada. Total: $${total.toInt()}",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }
}
