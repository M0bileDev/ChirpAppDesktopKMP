package com.example.core.designsystem.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.core.designsystem.generated.resources.Res
import chirpappkmp.core.designsystem.generated.resources.logo_chirp
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSurface(
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header()
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewChirpSurface() {
    ChirpTheme {
        ChirpSurface(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text(
                    "Lorem ipsum",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            },
            header = {
                Icon(
                    imageVector = vectorResource(Res.drawable.logo_chirp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpSurface() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpSurface(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text(
                    "Lorem ipsum",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            },
            header = {
                Icon(
                    imageVector = vectorResource(Res.drawable.logo_chirp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        )
    }
}