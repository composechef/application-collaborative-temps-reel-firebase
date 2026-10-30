package com.composechef.taskmate.data

import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.User
import kotlinx.coroutines.flow.Flow
import com.composechef.taskmate.core.model.ListInvite

interface ListRepository {
    fun observeLists(userId: String): Flow<List<SharedList>>

    suspend fun createList(owner: User, name: String, description: String, color: Int)

    fun observeList(listId: String): Flow<SharedList?>

    suspend fun deleteList(list: SharedList)

    suspend fun findInvite(code: String): ListInvite?

    suspend fun joinList(listId: String, user: User)
}
