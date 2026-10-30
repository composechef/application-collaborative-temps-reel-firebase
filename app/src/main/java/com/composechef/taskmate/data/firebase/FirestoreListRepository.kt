package com.composechef.taskmate.data.firebase

import com.composechef.taskmate.core.model.ListInvite
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.User
import com.composechef.taskmate.core.model.initialsOf
import com.composechef.taskmate.data.ListRepository
import com.composechef.taskmate.data.firebase.model.InviteDocument
import com.composechef.taskmate.data.firebase.model.ListDocument
import com.composechef.taskmate.data.firebase.model.MemberDocument
import com.composechef.taskmate.data.firebase.model.toDomain
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject


class FirestoreListRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : ListRepository {

    private val lists
        get() = firestore.collection("lists")

    private val invites
        get() = firestore.collection("invites")

    override fun observeLists(userId: String): Flow<List<SharedList>> = callbackFlow {
        val registration = lists
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val result = snapshot?.toObjects<ListDocument>()
                    .orEmpty()
                    .sortedWith(compareBy(nullsLast<Date>()) { it.createdAt })
                    .map { it.toDomain() }
                trySend(result)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun createList(owner: User, name: String, description: String, color: Int) {
        val listRef = lists.document()
        val code = newInviteCode()
        val list = ListDocument(
            name = name,
            description = description,
            color = color,
            code = code,
            ownerId = owner.id,
            memberIds = listOf(owner.id),
            members = mapOf(owner.id to MemberDocument(owner.name, initialsOf(owner.name))),
        )
        val invite = InviteDocument(listId = listRef.id, name = name, color = color)

        firestore.batch()
            .set(listRef, list)
            .set(invites.document(code), invite)
            .commit()
            .await()
    }

    override fun observeList(listId: String): Flow<SharedList?> {
        return lists.document(listId).snapshots().map { snapshot ->
            snapshot.toObject<ListDocument>()?.toDomain()
        }
    }

    override suspend fun deleteList(list: SharedList) {
        val listRef = lists.document(list.id)
        val tasks = listRef.collection("tasks").get().await()
        val batch = firestore.batch()
        tasks.documents.forEach { batch.delete(it.reference) }
        batch.delete(invites.document(list.code))
        batch.delete(listRef)
        batch.commit().await()
    }

    override suspend fun findInvite(code: String): ListInvite? {
        return invites.document(code).get().await().toObject<InviteDocument>()?.toDomain()
    }

    override suspend fun joinList(listId: String, user: User) {
        lists.document(listId)
            .update(
                mapOf(
                    "memberIds" to FieldValue.arrayUnion(user.id),
                    "members.${user.id}" to mapOf("name" to user.name, "initials" to initialsOf(user.name)),
                )
            )
            .await()
    }

    private fun newInviteCode(): String {
        return (1..ListInvite.CODE_LENGTH).map {
            INVITE_ALPHABET.random()
        }.joinToString(separator = "")
    }

    private companion object {
        /** Sans 0, O, 1 ni I, qu'on confond facilement en recopiant un code. */
        const val INVITE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    }
}
