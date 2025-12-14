package com.example.rmviewer.model

// Esta clase es solo para saber en qué página estoy.
data class Info(
    val count: Int, // Total de episodios que existen.
    val pages: Int, // Total de páginas.

    // El signo de interrogación '?' es CLAVE aquí.
    // Significa que puede venir nulo (null).
    // Por ejemplo: si estoy en la última página, 'next' no existe, así que evita que la app explote.
    val next: String?,
    val prev: String?
)