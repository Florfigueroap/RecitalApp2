package com.unlam.recitalapp2.domain

enum class MetodoPagoOpcion(val descripcion: String) {
    MERCADO_PAGO("Mercado Pago"),
    VISA("Visa"),
    MASTERCARD("MasterCard");

    override fun toString(): String = descripcion
}
