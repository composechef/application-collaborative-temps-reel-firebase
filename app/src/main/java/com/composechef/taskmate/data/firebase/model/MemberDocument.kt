package com.composechef.taskmate.data.firebase.model

import com.composechef.taskmate.core.model.Member

data class MemberDocument(
    val name: String = "",
    val initials: String = "",
)

fun MemberDocument.toDomain(id: String): Member {
    return Member(
        id = id,
        name = name,
        initials = initials
    )
}
