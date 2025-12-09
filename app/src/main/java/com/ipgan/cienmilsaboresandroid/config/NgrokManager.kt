package com.ipgan.cienmilsaboresandroid.config

import android.content.Context
import com.ipgan.cienmilsaboresandroid.remote.RetrofitInstance2

object NgrokManager {

    private const val PREFS_NAME = "app_config"
    private const val PREF_NGROK_URL = "ngrok_url"
    // Una URL por defecto que sabes que no funciona, para forzar al usuario a cambiarla.
    private const val DEFAULT_URL = "https://cambiame.ngrok.io/"

    /**
     * Obtiene la URL base guardada. Si no hay ninguna, devuelve la URL por defecto.
     * Siempre se asegura de que termine con '/'.
     */
    fun getBaseUrl(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedUrl = prefs.getString(PREF_NGROK_URL, DEFAULT_URL) ?: DEFAULT_URL
        return if (savedUrl.endsWith("/")) savedUrl else "$savedUrl/"
    }

    /**
     * Actualiza la URL de Ngrok, la guarda en SharedPreferences y reinicia Retrofit.
     */
    fun updateNgrokUrl(context: Context, newUrl: String) {
        // Nos aseguramos de que la URL termine con '/' antes de guardarla.
        val formattedUrl = if (newUrl.endsWith("/")) newUrl else "$newUrl/"

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_NGROK_URL, formattedUrl).apply()

        // Le decimos a Retrofit que su configuración ya no es válida y debe recrearse.
        RetrofitInstance2.invalidate()
    }
}
