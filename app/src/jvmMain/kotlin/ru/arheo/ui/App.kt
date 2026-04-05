package ru.arheo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) {
        RootContent(Modifier.fillMaxSize())
    }
}