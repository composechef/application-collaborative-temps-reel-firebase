package com.composechef.testmate.core.model

data class SharedCollection(
    val id: String,
    val name: String,
    val description: String,
    val color: Int,
    val symbol: String,
    val code: String,
    val members: List<Member>,
)
