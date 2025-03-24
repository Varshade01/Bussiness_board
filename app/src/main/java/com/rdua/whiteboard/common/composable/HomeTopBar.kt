package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rdua.whiteboard.R
import com.rdua.whiteboard.ui.theme.MaterialTheme

/**
 * Home Top Bar.
 *
 * A layout composable for display information and actions at the top of a screen.
 * This layout has slots for a title, a navigation actions, and a search field.
 * The Navigation Bar is disabled if the navigation back stack is null.
 * The SearchTextField Bar is disabled if the onSearchValueChange is null.
 *
 * @param titleTopBar the title to be displayed in the top app bar.
 * @param onBackPressed called when the navigate back button in the Navigation Bar is clicked.
 * @param navigationActionsContent the actions displayed at the end of the Navigation Bar. This should typically be
 * [IconButton]s. The default layout here is a [Row], so icons inside will be placed horizontally.
 * @param searchText the input text to be shown in the search text field.
 * @param onClickSearchMicrophone called when a microphone icon button in the search bar is clicked
 * @param onSearchValueChange the callback that is triggered when the input service updates the search text.
 * An updated text comes as a parameter of the callback.
 * If the onSearchValueChange is null, the SearchTextField Bar is disabled.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    titleTopBar: String,
    onBackPressed: (() -> Unit) = {},
    navigationActionsContent: @Composable RowScope.() -> Unit = {},
    searchText: String = "",
    onClickSearchMicrophone: () -> Unit = {},
    onSearchValueChange: ((String) -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        TopAppBar(
            title = { TitleBar(titleTopBar) },
            navigationIcon = { BackButton(onBackPressed) },
            actions = navigationActionsContent,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        if (onSearchValueChange != null) {
            SearchTextFieldBar(
                searchText = searchText,
                onClickMicrophone = onClickSearchMicrophone,
                onValueChange = onSearchValueChange
            )
        }
        HomeDivider()
    }
}

@Composable
private fun TitleBar(titleTopBar: String) {
    Text(
        text = titleTopBar,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = MaterialTheme.spaces.space4),
    )
}

@Composable
private fun SearchTextFieldBar(
    searchText: String,
    onClickMicrophone: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.spaces.space4,
                end = MaterialTheme.spaces.space4,
                bottom = MaterialTheme.spaces.space3
            ),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colors.searchTextFieldContainer,
            unfocusedContainerColor = MaterialTheme.colors.searchTextFieldContainer,
            focusedPlaceholderColor = MaterialTheme.colors.searchTextFieldPlaceholder,
            unfocusedPlaceholderColor = MaterialTheme.colors.searchTextFieldPlaceholder,
        ),
        shape = RoundedCornerShape(30),
        textStyle = MaterialTheme.typography.titleMedium,
        placeholder = { Text(text = stringResource(R.string.search)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(R.string.search),
                tint = Color.Unspecified,
            )
        },
        trailingIcon = {
            IconButton(onClick = onClickMicrophone) {
                Icon(
                    painter = painterResource(R.drawable.ic_microphone),
                    contentDescription = stringResource(R.string.microphone),
                    tint = Color.Unspecified,
                )
            }
        },
        value = searchText,
        onValueChange = onValueChange
    )
}

@Preview
@Composable
fun HomeTopBarPreview() {
    HomeTopBar(
        titleTopBar = "All Board",
        navigationActionsContent = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null,
                )
            }
        }
    )
}