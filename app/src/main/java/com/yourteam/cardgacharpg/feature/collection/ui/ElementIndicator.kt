package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Element

// Owner: Person 1 (Leila) — Accessibility: Element war bisher nur als reiner Text-Chip da.
// Ergänzt ein eindeutiges Symbol je Element (Unicode statt Icon-Library, keine neue Dependency).
fun Element.symbol(): String = when (this) {
    Element.FEUER -> "▲"
    Element.NATUR -> "❋"
    Element.WASSER -> "●"
    Element.ARKAN -> "◆"
}

fun Element.germanLabel(): String = when (this) {
    Element.FEUER -> "Feuer"
    Element.NATUR -> "Natur"
    Element.WASSER -> "Wasser"
    Element.ARKAN -> "Arkan"
}

@Composable
fun ElementChip(element: Element, modifier: Modifier = Modifier) {
    AssistChip(
        onClick = {},
        modifier = modifier,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(element.symbol())
                Spacer(Modifier.width(4.dp))
                Text(element.germanLabel())
            }
        }
    )
}