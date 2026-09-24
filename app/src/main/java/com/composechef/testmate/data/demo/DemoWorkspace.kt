package com.composechef.testmate.data.demo

import com.composechef.testmate.core.model.DueDate
import com.composechef.testmate.core.model.Member
import com.composechef.testmate.core.model.SharedCollection
import com.composechef.testmate.core.model.Task

/** Sample content used only by the local UI preview. */
internal object DemoWorkspace {
    fun collections(me: Member, language: String = "fr"): List<SharedCollection> {
        val team = listOf(me, Member("sam", "Sam", "SK"), Member("jules", "Jules", "JL"))
        return listOf(
            SharedCollection(
                "launch",
                if (language == "en") "Studio launch" else "Lancement du studio",
                if (language == "en") "Prepare the studio launch."
                else "Préparer le lancement du studio.",
                0,
                "✦",
                "STUDIO",
                team,
            ),
            SharedCollection(
                "home",
                if (language == "en") "A place called home" else "À la maison",
                if (language == "en") "Organize projects around the house."
                else "Organiser les projets de la maison.",
                1,
                "⌂",
                "HOMELY",
                team.take(2),
            ),
            SharedCollection(
                "weekend",
                if (language == "en") "The weekend edit" else "Week-end",
                if (language == "en") "Plan our next outings."
                else "Préparer nos prochaines sorties.",
                2,
                "☀",
                "WKND26",
                team,
            ),
        )
    }

    fun tasks(language: String = "fr"): List<Task> =
        listOf(
            Task(
                collectionId = "launch",
                title =
                    if (language == "en") "Put together the moodboard"
                    else "Créer le tableau d’inspiration",
                assignee = "me",
                due = DueDate.Today,
            ),
            Task(
                collectionId = "launch",
                title =
                    if (language == "en") "Explore a few logo directions"
                    else "Proposer des pistes de logo",
                notes =
                    if (language == "en") "Keep the style simple and welcoming."
                    else "Privilégier un style simple et chaleureux.",
                assignee = "sam",
                due = DueDate.Tomorrow,
            ),
            Task(
                collectionId = "launch",
                title = if (language == "en") "Write our story" else "Rédiger notre présentation",
                assignee = "me",
                due = DueDate.Tomorrow,
            ),
            Task(
                collectionId = "launch",
                title = if (language == "en") "Find our north star" else "Définir nos objectifs",
                assignee = "jules",
                completed = true,
            ),
            Task(
                collectionId = "home",
                title = if (language == "en") "Pick up a few plants" else "Acheter des plantes",
                assignee = "me",
                due = DueDate.Today,
            ),
            Task(
                collectionId = "home",
                title =
                    if (language == "en") "Set up the reading corner"
                    else "Aménager le coin lecture",
                assignee = "sam",
            ),
            Task(
                collectionId = "home",
                title =
                    if (language == "en") "Make a moving playlist"
                    else "Préparer la playlist du déménagement",
                completed = true,
            ),
            Task(
                collectionId = "weekend",
                title =
                    if (language == "en") "Find a new hiking trail"
                    else "Choisir un itinéraire de randonnée",
                assignee = "jules",
                due = DueDate.ThisWeek,
            ),
            Task(
                collectionId = "weekend",
                title =
                    if (language == "en") "Book a table for brunch"
                    else "Réserver une table pour le brunch",
                assignee = "me",
                due = DueDate.ThisWeek,
            ),
        )

    fun invitedCollection(me: Member, language: String = "fr"): SharedCollection =
        SharedCollection(
            "garden",
            if (language == "en") "Community garden" else "Jardin partagé",
            if (language == "en") "Organize garden activities."
            else "Organiser les activités du jardin.",
            1,
            "✿",
            "TOGETH",
            listOf(me, Member("mia", "Mia", "ML")),
        )
}
