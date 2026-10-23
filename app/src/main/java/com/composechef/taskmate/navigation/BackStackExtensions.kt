package com.composechef.taskmate.navigation

import androidx.navigation3.runtime.NavKey

/** Remplace l'écran du dessus de la pile, par exemple pour passer de la connexion à l'inscription. */
fun MutableList<NavKey>.replaceLast(key: NavKey) {
    removeLastOrNull()
    add(key)
}


/** Remplace toute la pile par l'onglet [key]. */
fun MutableList<NavKey>.switchTab(key: NavKey) {
    clear()
    add(key)
}
