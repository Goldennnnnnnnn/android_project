package com.example.aotexplorer.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val info: Info,
    val results: List<T>
)

@Serializable
data class Info(
    val count: Int = 0,
    val pages: Int = 0,
    val next: String? = null,
    val prev: String? = null
)
