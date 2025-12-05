package io.github.p1k0chu.bacaptrackersheetgenerator.utils

import java.util.Locale.getDefault

fun getTabName(id: String): String {
    return when (id) {
        "animal" -> "Animals"
        "challenges" -> "Super Challenges"
        "potion" -> "Potions"
        else -> id.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
    }
}