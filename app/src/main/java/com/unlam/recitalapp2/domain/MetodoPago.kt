package com.unlam.recitalapp2.domain

abstract class MetodoPago {
    /**
     * @param montoBase monto base (precio*cantidad)
     * @param hora hora 0..23 (regla de VISA)
     * @param minuto minuto 0..59 (regla de VISA)
     * @param diaSemana 1=lun ... 7=dom (regla de MasterCard)
     */
    abstract fun total(montoBase: Double, hora: Int, minuto: Int, diaSemana: Int): Double
}

// Mercado Pago se hace +2% siempre
object MercadoPago : MetodoPago() {
    override fun total(montoBase: Double, hora: Int, minuto: Int, diaSemana: Int): Double {
        return montoBase * 1.02
    }
}

// Visa: +1% entre 15:00 y 22:30. fuera de ese rango +3%
object Visa : MetodoPago() {
    override fun total(montoBase: Double, hora: Int, minuto: Int, diaSemana: Int): Double {
        val totalMin = hora * 60 + minuto
        val from = 15 * 60 + 0
        val to   = 22 * 60 + 30
        val inRange = (totalMin >= from && totalMin <= to)
        return if (inRange) montoBase * 1.01 else montoBase * 1.03
    }
}

// MasterCard: sáb/dom +3% y días de semana +0.75%
object MasterCard : MetodoPago() {
    override fun total(montoBase: Double, hora: Int, minuto: Int, diaSemana: Int): Double {
        val finde = (diaSemana == 6 || diaSemana == 7)
        return if (finde) montoBase * 1.03 else montoBase * 1.0075
    }
}
