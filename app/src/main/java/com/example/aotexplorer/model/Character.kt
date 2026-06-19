package com.example.aotexplorer.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val img: String? = null,
    val alias: List<String>? = null,
    val species: List<String>? = null,
    val gender: String? = null,
    @Serializable(with = AgeSerializer::class)
    val age: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val birthplace: String? = null,
    val residence: String? = null,
    val status: String? = null,
    val occupation: String? = null,
    val roles: List<String>? = null,
    val regiment: String? = null // Some versions of the API might have this, or it's mapped from groups
)
