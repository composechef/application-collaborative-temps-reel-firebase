package com.composechef.taskmate.data.firebase.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import com.composechef.taskmate.core.model.SharedList


data class ListDocument(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val color: Int = 0,
    val code: String = "",
    val ownerId: String = "",
    val memberIds: List<String> = emptyList(),
    val members: Map<String, MemberDocument> = emptyMap(),
    @ServerTimestamp val createdAt: Date? = null,
)

fun ListDocument.toDomain(): SharedList {
    return SharedList(
        id = id,
        name = name,
        description = description,
        color = color,
        code = code,
        ownerId = ownerId,
        members = memberIds.mapNotNull { memberId ->
            members[memberId]?.toDomain(memberId)
        },
    )
}
