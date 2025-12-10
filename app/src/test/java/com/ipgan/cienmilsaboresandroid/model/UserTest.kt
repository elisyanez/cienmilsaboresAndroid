package com.ipgan.cienmilsaboresandroid.model

import org.junit.Assert.*
import org.junit.Test

class UserTest {

    @Test
    fun `creación de usuario con datos completos`() {
        // 1. Preparación (Arrange)
        val usuario = User(
            run = "12345678-9",
            name = "Juan",
            lastName = "Pérez",
            email = "juan.perez@example.com",
            password = "password123",
            address = "Calle Falsa 123",
            role = "cliente",
            region = "Metropolitana",
            commune = "Santiago"
        )

        // 2. Actuación (Act) - No se necesita

        // 3. Afirmación (Assert)
        assertEquals("12345678-9", usuario.run)
        assertEquals("Juan", usuario.name)
        assertEquals("Pérez", usuario.lastName)
        assertEquals("juan.perez@example.com", usuario.email)
        assertEquals("cliente", usuario.role)
    }

    @Test
    fun `creación de usuario con rol de administrador y campos opcionales nulos`() {
        // 1. Preparación (Arrange)
        val admin = User(
            run = "87654321-K",
            name = "Admin",
            lastName = "Maestro",
            email = "admin@cienmilsabores.com",
            password = "adminpassword",
            address = null,
            role = "admin",
            region = null,
            commune = null
        )

        // 2. Actuación (Act) - No se necesita

        // 3. Afirmación (Assert)
        assertEquals("admin", admin.role)
        assertEquals("87654321-K", admin.run)
        assertNull(admin.address)
        assertNull(admin.region)
    }

    @Test
    fun `la función copy crea una copia con valores modificados`() {
        // 1. Preparación (Arrange)
        val usuarioOriginal = User(
            run = "11222333-4",
            name = "Ana",
            lastName = "González",
            email = "ana@example.com",
            password = "password",
            address = "Av. Siempre Viva 742",
            role = "cliente",
            region = "Valparaíso",
            commune = "Viña del Mar"
        )

        // 2. Actuación (Act)
        val usuarioCopiado = usuarioOriginal.copy(email = "ana.gonzalez@work.com", address = "Nueva Dirección 456")

        // 3. Afirmación (Assert)
        assertEquals("11222333-4", usuarioCopiado.run) // El RUN no cambia
        assertEquals("Ana", usuarioCopiado.name) // El nombre no cambia
        assertEquals("ana.gonzalez@work.com", usuarioCopiado.email) // El email sí
        assertEquals("Nueva Dirección 456", usuarioCopiado.address) // La dirección sí
        assertNotEquals(usuarioOriginal, usuarioCopiado)
    }
}