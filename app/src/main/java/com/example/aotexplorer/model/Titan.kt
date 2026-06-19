package com.example.aotexplorer.model

import kotlinx.serialization.Serializable

@Serializable
data class Titan(
    val id: Int,
    val name: String,
    val img: String? = null,
    val abilities: List<String>? = null,
    val current_inheritor: String? = null,
    val former_inheritors: List<String>? = null,
    val allegiance: String? = null
)
