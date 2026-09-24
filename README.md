# TaskMate

A collaborative to-do UI prototype, built with Jetpack Compose, Material 3, and Navigation 3.

## Explore

Run the `app` configuration in Android Studio. Choose **Try demo** on the welcome screen to explore a populated workspace, or use the signup/signin forms to start an empty local session. Forms accept a valid email and a password of at least eight characters; no credentials are stored or sent.

- Create collections with a name, description, and visual identity.
- Join a sample collection using **TOGETH**, with a preview before joining.
- Add and edit tasks, assign members, choose a relative due date, and complete tasks with Undo.
- Filter collection tasks and see your assignments across collections.
- View members, copy invitation codes, or open Android’s share sheet.
- Single-column collection list, navigation rail on wide windows, light/dark themes, and phone/tablet previews.
- Swipe left to reveal Delete on tasks or collections; swipe right to complete unfinished tasks.
- Undo task deletion, or confirm permanent collection deletion (including its tasks).
- Delete actions are also available in task details and the collection options menu.

## Code structure

Source packages live under `app/src/main/java/com/composechef/testmate`:

```text
MainActivity.kt                 Android entry point
app/
  TaskMateApp.kt                 App composition and navigation state
  WorkspaceScreen.kt             Adaptive navigation and feature composition
  WorkspaceViewModel.kt          Session state and workspace actions
  navigation/                    Destinations, sheet types, and sheet host
core/
  model/                         Member, SharedCollection, and Task
  designsystem/
    components/                  Avatars, headings, empty states, sheet container
    theme/                       Material color, typography, and shapes
data/
  demo/                          Sample workspace and invitation fixtures
feature/
  auth/                          Welcome, signin, signup, and reset UI
  collections/                   Collection screens, forms, members, and previews
    components/                  Collection cards
  tasks/                         Personal tasks and task editor
    components/                  Shared task rows
  profile/                       Profile and sign-out confirmation
```

Each screen, sheet, reusable component, and model has its own file. Feature screens
receive values and callbacks; the app package connects them to session state.
Shared UI belongs in `core/designsystem`, while feature-specific components stay
with their feature. Demo content belongs in `data/demo`, separate from the ViewModel.

Tests mirror the owning package under `app/src/test/java`; workspace behavior tests
live in `app/WorkspaceViewModelTest.kt`. This remains a single Gradle module.

The workspace survives configuration changes but is intentionally not persisted after process termination. Authentication, reset emails, remote collaboration, and invitation redemption are simulated. Relative due dates are prototype labels, not calendar dates. Other members are sample data. Signup and signin do not establish real accounts.

## Verification

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
# With an emulator or device connected:
./gradlew :app:connectedDebugAndroidTest
```

Unit tests cover session isolation, collection joining, task updates, signout, deletion, and restoration. Instrumented Compose tests exercise swipe reveal, completion, Undo, confirmation, and non-gesture deletion. Compose previews cover phone, tablet, and dark appearance.

## Languages

French is selected on first launch, including on English-language devices. Choose
Français or English from Welcome or Profile; Android remembers the app language.
Android 13+ also exposes the supported languages in system app settings.

UI copy, plural counts, accessibility labels, and invitations use Android resources:
`res/values` (French fallback), `res/values-fr`, and `res/values-en`.
Due dates use a stable `DueDate` enum, with localized labels applied only in the UI.
Demo content is seeded in the language selected when starting the session. Switching
languages does not translate or overwrite collection names, task titles, or notes.

Instrumented tests run the swipe/delete/Undo flows in both languages and verify that
language switching and Activity recreation preserve the current workspace.
