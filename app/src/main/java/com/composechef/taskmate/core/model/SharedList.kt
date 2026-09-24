package com.composechef.taskmate.core.model

data class SharedList(
    val id: String,
    val name: String,
    val description: String,
    val color: Int,
    val code: String,
    val ownerId: String,
    val members: List<Member>,
)
