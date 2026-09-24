# TaskMate — projet de départ

Projet de départ du codelab ComposeChef **« Créer une application collaborative en temps réel avec Firebase »**.

Cette branche (`starter`) contient uniquement l'interface de l'app : les écrans, les sheets, les dialogues, le thème, les modèles et les textes en français et en anglais. Chaque écran est une fonction Compose sans état qui reçoit un `UiState` et des callbacks, avec ses previews.

Il n'y a encore ni navigation, ni ViewModel, ni repository, ni Firebase : tu les construis pas à pas dans le codelab. La branche `final` contient l'app terminée.

## Lancer le projet

Ouvre le dossier dans une version récente d'Android Studio et lance la configuration `app`. L'app affiche l'écran d'accueil, et ses boutons ne font encore rien.

Pour voir les autres écrans, ouvre leurs previews (par exemple `feature/lists/ListsScreen.kt`) dans la vue *Split* d'Android Studio.

## Structure

```text
com/composechef/taskmate/
  MainActivity.kt
  core/
    model/          User, Member, SharedList, Task, DueDate, ListInvite
    designsystem/   thème et composants partagés (HomeScaffold, SwipeActions, TaskMateSheet…)
    localization/   libellés des échéances
    preview/        données d'exemple pour les previews
  feature/
    auth/           accueil, connexion, inscription, mot de passe oublié
    lists/          listes, détail d'une liste, création, invitation, membres
    tasks/          « Mes tâches » et éditeur de tâche
    profile/        profil et déconnexion
```

## Langues

Le français est la langue par défaut (`res/values`). L'anglais est dans `res/values-en`.
