package com.example.core.designsystem.components.dropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.theme.extended

@Composable
fun ChirpDropDownMenu(
    isOpen: Boolean,
    items: List<DropDownItem>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isOpen,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extended.surfaceOutline
        )
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = item.contentColor
                        )
                        Text(
                            text = item.title,
                            color = item.contentColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = item.onClick
            )
            if (index != items.lastIndex) {
                ChirpHorizontalDivider()
            }
        }
    }
}

//there is a bug with dropdown, no previews