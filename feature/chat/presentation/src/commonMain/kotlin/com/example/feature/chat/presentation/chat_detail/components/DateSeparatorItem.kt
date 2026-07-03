package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DateSeparatorItem(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            text = date,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.textPlaceholder
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun PreviewDateSeparatorItem() {
    ChirpTheme {
        DateSeparatorItem(
            date = "Lorem ipsum"
        )
    }
}

@Preview
@Composable
fun PreviewDarkDateSeparatorItem() {
    ChirpTheme(
        darkTheme = true
    ) {
        DateSeparatorItem(
            date = "Lorem ipsum"
        )
    }
}