package com.rdua.whiteboard.navigation.destinations

/**
 * Encapsulates destination’s route information.
 *
 * @param route the unique route to the destination. To be used when navigating.
 * @param params the list of mandatory parameter names to add to the route to get the full route.
 * @property fullRoute the full route to the destination, including possible parameters. To be used when providing destinations inside NavGraph.
 */
sealed class Destination (val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{$it}") }
        builder.toString()
    }

    protected fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    object SplashScreen : Destination("splash_screen")
    object RegistrationScreen : Destination("register_screen")
    object LoginScreen : Destination("login_screen")
    object ResetPasswordScreen : Destination("reset_password_screen")
    object HomeScreen : Destination("home_screen")
    object ProfileScreen : Destination("profile_screen_non_edit")

    object BoardScreen : Destination("board_screen", "board_id") {
        const val BOARD_ID_KEY = "board_id"
        operator fun invoke(boardId: String) : String = withArgs(boardId)
    }

    object Settings : Destination("settings_screen")
    object EditProfileScreen : Destination("edit_profile_screen")
    object ChangePassword : Destination("change_password_screen")
}