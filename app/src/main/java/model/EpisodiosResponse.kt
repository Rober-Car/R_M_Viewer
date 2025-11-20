package model

data class EpisodiosResponse(
    val info: Info,
    val results: List<Episode>
)