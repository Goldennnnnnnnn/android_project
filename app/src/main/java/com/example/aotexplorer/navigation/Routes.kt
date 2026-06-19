package com.example.aotexplorer.navigation

object Routes {
    const val CHARACTER_LIST = "character_list"
    const val CHARACTER_DETAIL = "character_detail/{characterId}"
    const val TITANS = "titans"

    fun characterDetail(id: Int) = "character_detail/$id"
}
