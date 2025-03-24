package com.rdua.whiteboard.navigation.di

import com.rdua.whiteboard.edit_profile.navigation.EditProfileNavigationActions
import com.rdua.whiteboard.change_password.navigation.ChangePasswordNavigationActions
import com.rdua.whiteboard.home.navigation.IHomeNavigationActions
import com.rdua.whiteboard.login.navigation.LoginNavigationActions
import com.rdua.whiteboard.navigation.actions.EditProfileNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.ChangePasswordNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.HomeNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.LoginNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.BoardItemNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.ProfileScreenNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.RegistrationNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.ResetPasswordNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.SettingsNavigationActionsImpl
import com.rdua.whiteboard.navigation.actions.SplashNavigationActionsImpl
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.navigation.navigator.AppNavigatorImpl
import com.rdua.whiteboard.board_item.navigation.IBoardItemNavigationActions
import com.rdua.whiteboard.board_options_dialog.navigation.BoardOptionsNavigationActions
import com.rdua.whiteboard.navigation.actions.BoardOptionsNavigationActionsImpl
import com.rdua.whiteboard.profile_screen_non_edit.navigation.ProfileScreenNavigationActions
import com.rdua.whiteboard.registration.navigation.RegistrationNavigationActions
import com.rdua.whiteboard.reset_password.navigation.ResetPasswordNavigationActions
import com.rdua.whiteboard.settings.navigation.ISettingsNavigationActions
import com.rdua.whiteboard.splash.navigation.SplashNavigationActions
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Singleton
    @Binds
    abstract fun bindAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator

    @Singleton
    @Binds
    abstract fun bindSplashNavigationActions(
        splashNavigationActions: SplashNavigationActionsImpl
    ): SplashNavigationActions

    @Singleton
    @Binds
    abstract fun bindLoginNavigationActions(
        loginNavigationActions: LoginNavigationActionsImpl
    ): LoginNavigationActions

    @Singleton
    @Binds
    abstract fun bindRegistrationNavigationActions(
        loginNavigationActions: RegistrationNavigationActionsImpl
    ): RegistrationNavigationActions

    @Singleton
    @Binds
    abstract fun bindResetPasswordNavigationActions(
        resetPasswordNavigationActions: ResetPasswordNavigationActionsImpl,
    ): ResetPasswordNavigationActions

    @Singleton
    @Binds
    abstract fun bindProfileNonEditNavigationActions(
        profileScreenNavigationActionsImpl: ProfileScreenNavigationActionsImpl,
    ): ProfileScreenNavigationActions

    @Singleton
    @Binds
    abstract fun bindHomeNavigationActions(
        homeNavigationActions: HomeNavigationActionsImpl,
    ): IHomeNavigationActions

    @Singleton
    @Binds
    abstract fun bindBoardItemNavigationActions(
        boardItemNavigationActions: BoardItemNavigationActionsImpl,
    ): IBoardItemNavigationActions

    @Singleton
    @Binds
    abstract fun bindEditProfileScreenNavigationActions(
        editProfileNavigationActions: EditProfileNavigationActionsImpl
    ): EditProfileNavigationActions

    @Singleton
    @Binds
    abstract fun bindSettingsNavigationActions(
        settingsNavigationActions: SettingsNavigationActionsImpl,
    ): ISettingsNavigationActions

    @Singleton
    @Binds
    abstract fun bindChangePasswordNavigationActions(
        changePasswordNavigationActionsImpl: ChangePasswordNavigationActionsImpl
    ): ChangePasswordNavigationActions

    @Singleton
    @Binds
    abstract fun bindBoardOptionsNavigationActions(
        boardOptionsNavigationActionsImpl: BoardOptionsNavigationActionsImpl
    ): BoardOptionsNavigationActions
}
