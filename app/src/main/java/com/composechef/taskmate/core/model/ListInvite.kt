package com.composechef.taskmate.core.model

/** Ce qu'une personne voit d'une liste avant de la rejoindre. */
data class ListInvite(
    val code: String,
    val listId: String,
    val name: String,
    val color: Int,
) {
    companion object {
        /** Nombre de caractères d'un code d'invitation. */
        const val CODE_LENGTH = 6
    }
}
