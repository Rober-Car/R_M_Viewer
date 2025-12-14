package com.example.rmviewer.model

// Esta es la clase "padre". Representa TODO el JSON que recibo de golpe.
data class EpisodiosResponse(
    // Aquí viene la info de paginación (cuántas páginas hay, etc.)
    val info: Info,

    // Aquí va la lista de capítulos.
    // Importante: El tipo dentro de < > tiene que ser la clase que creé abajo (InfoEpisodio).
    // Antes ponía 'Episodio' y daba error porque esa clase no existía.
    val results: List<Episodio>
)