package com.composechef.taskmate.core.model

/** Échéances relatives. Leur libellé traduit est choisi dans l'UI (voir DueDateLabel.kt). */
enum class DueDate {
    None,
    Today,
    Tomorrow,
    ThisWeek,
}
