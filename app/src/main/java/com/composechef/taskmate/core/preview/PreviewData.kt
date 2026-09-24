package com.composechef.taskmate.core.preview

import com.composechef.taskmate.core.model.DueDate
import com.composechef.taskmate.core.model.ListInvite
import com.composechef.taskmate.core.model.Member
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.model.Task
import com.composechef.taskmate.core.model.User

/** Données d'exemple utilisées uniquement par les previews Compose. */
internal object PreviewData {
    val alex = Member(id = "alex", name = "Alex Morgan", initials = "AM")
    val sam = Member(id = "sam", name = "Sam Kalala", initials = "SK")
    val jules = Member(id = "jules", name = "Jules Lambert", initials = "JL")

    val user = User(id = alex.id, name = alex.name, email = "alex@example.com")

    val launch =
        SharedList(
            id = "launch",
            name = "Lancement du studio",
            description = "Préparer le lancement du studio.",
            color = 0,
            code = "STUDIO",
            ownerId = alex.id,
            members = listOf(alex, sam, jules),
        )

    val home =
        SharedList(
            id = "home",
            name = "À la maison",
            description = "Organiser les projets de la maison.",
            color = 1,
            code = "HOMELY",
            ownerId = sam.id,
            members = listOf(alex, sam),
        )

    val weekend =
        SharedList(
            id = "weekend",
            name = "Week-end",
            description = "Préparer nos prochaines sorties.",
            color = 2,
            code = "WKND26",
            ownerId = alex.id,
            members = listOf(alex, sam, jules),
        )

    val lists = listOf(launch, home, weekend)

    val tasks =
        listOf(
            Task(
                id = "moodboard",
                listId = launch.id,
                title = "Créer le tableau d’inspiration",
                assigneeId = alex.id,
                due = DueDate.Today,
            ),
            Task(
                id = "logo",
                listId = launch.id,
                title = "Proposer des pistes de logo",
                assigneeId = sam.id,
                due = DueDate.Tomorrow,
            ),
            Task(
                id = "venue",
                listId = launch.id,
                title = "Réserver la salle",
                assigneeId = alex.id,
                due = DueDate.ThisWeek,
                completed = true,
            ),
            Task(
                id = "tap",
                listId = home.id,
                title = "Réparer le robinet de la cuisine",
                assigneeId = alex.id,
                due = DueDate.ThisWeek,
            ),
            Task(id = "restaurant", listId = weekend.id, title = "Choisir le restaurant"),
        )

    val invite = ListInvite(code = "TOGETH", listId = "garden", name = "Jardin partagé", color = 3)
}
