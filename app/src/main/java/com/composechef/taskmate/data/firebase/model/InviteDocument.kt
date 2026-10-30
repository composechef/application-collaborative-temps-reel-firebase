package com.composechef.taskmate.data.firebase.model

import com.composechef.taskmate.core.model.ListInvite
import com.google.firebase.firestore.DocumentId

data class InviteDocument(
    @DocumentId val code: String = "",
    val listId: String = "",
    val name: String = "",
    val color: Int = 0,
)

fun InviteDocument.toDomain(): ListInvite {
    return ListInvite(
        code = code,
        listId = listId,
        name = name,
        color = color
    )
}
