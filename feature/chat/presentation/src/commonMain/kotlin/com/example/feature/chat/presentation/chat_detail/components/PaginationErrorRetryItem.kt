package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.retry
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PaginationErrorRetryItem(
    paginationErrorText: String,
    modifier: Modifier = Modifier,
    onPaginationRetryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChirpButton(
            text = stringResource(Res.string.retry),
            onClick = onPaginationRetryClick,
            style = ChirpButtonStyle.SECONDARY
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = paginationErrorText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun PreviewPaginationErrorRetryItem() {
    ChirpTheme {
        PaginationErrorRetryItem(
            paginationErrorText = "Lorem ipsum",
            onPaginationRetryClick = {}
        )
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun PreviewDarkPaginationErrorRetryItem() {
    ChirpTheme(darkTheme = true) {
        PaginationErrorRetryItem(
            paginationErrorText = "Lorem ipsum",
            onPaginationRetryClick = {}
        )
    }
}