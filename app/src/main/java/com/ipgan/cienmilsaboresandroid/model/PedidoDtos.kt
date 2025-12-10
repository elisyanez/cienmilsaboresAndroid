package com.ipgan.cienmilsaboresandroid.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// --- DTOs para la creación de pedidos ---

/**
 * Representa un item en el carrito para la solicitud de creación de pedido.
 * Ahora contiene el objeto Product completo, como lo espera el backend.
 * Produce JSON: { "producto": { "codigo": "...", "nombre": "...", ... }, "cantidad": N, "mensajePersonalizado": null }
 */
data class ItemCarritoRequest(
    @SerializedName("producto")
    val producto: Product, // Se envía el objeto Product completo
    @SerializedName("cantidad")
    val cantidad: Int,
    @SerializedName("mensajePersonalizado")
    val mensajePersonalizado: String? = null // Se añade para que coincida con el backend
)

/**
 * El DTO principal para la solicitud de creación de pedido.
 */
data class CrearPedidoRequest(
    @SerializedName("usuarioRun")
    val usuarioRun: String,
    @SerializedName("items")
    val items: List<ItemCarritoRequest> // Usa el nuevo DTO de item
)


// --- DTOs para representar los datos de un pedido ya creado ---

enum class EstadoPedido {
    PENDIENTE, CONFIRMADO, EN_PREPARACION, LISTO_PARA_ENVIO, EN_CAMINO, ENTREGADO, CANCELADO
}

data class DetallePedido(
    @SerializedName("id")
    val id: Long,
    @SerializedName("producto")
    val producto: Product,
    @SerializedName("cantidad")
    val cantidad: Int,
    @SerializedName("precioUnitario")
    val precioUnitario: BigDecimal
)

data class Pedido(
    @SerializedName("id")
    val id: Long,
    @SerializedName("numeroPedido")
    val numeroPedido: String,
    @SerializedName("fecha")
    val fecha: String, // Usamos String para simplicidad en el cliente
    @SerializedName("usuario")
    val usuario: User,
    @SerializedName("items")
    val items: List<DetallePedido>,
    @SerializedName("subtotal")
    val subtotal: BigDecimal,
    @SerializedName("iva")
    val iva: BigDecimal,
    @SerializedName("total")
    val total: BigDecimal,
    @SerializedName("estado")
    val estado: EstadoPedido,
    @SerializedName("direccionEnvio")
    val direccionEnvio: String,
    @SerializedName("regionEnvio")
    val regionEnvio: String,
    @SerializedName("comunaEnvio")
    val comunaEnvio: String
)

data class ActualizarEstadoRequest(
    @SerializedName("nuevoEstado")
    val nuevoEstado: EstadoPedido
)
