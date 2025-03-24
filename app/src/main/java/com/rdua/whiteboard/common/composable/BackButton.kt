package com.rdua.whiteboard.common.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.navigation.LocalNavController


@Composable
fun BackButton(
    onBackPressed: () -> Unit,
) {
    if (LocalNavController.current?.previousBackStackEntry != null) {
        IconButton(onClick = onBackPressed) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_arrow_description)
            )
        }
    }
}
