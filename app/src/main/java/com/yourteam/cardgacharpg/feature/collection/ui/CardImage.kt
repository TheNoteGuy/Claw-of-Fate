package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

// Owner: Person 1 (Leila)
// Zentrale Komponente zum Rendern eines Karten-Bilds (Collection-Grid, Kartendetail,
// Pull-Result, Arena-Tiles etc.). Löst Card.imageAssetName über CardImageProvider
// in eine echte Drawable-Resource auf.
//
// Solange für einen imageAssetName noch kein Asset existiert, wird ein einfacher
// Text-Platzhalter (erster Buchstabe) angezeigt, statt die App abstürzen zu lassen —
// wichtig, weil Person 2 (Gacha) neue Helden anlegen kann, bevor die PNGs fertig sind.
@Composable
fun CardImage(
    imageAssetName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val resId = CardImageProvider.resFor(imageAssetName)

    if (resId != null) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contentDescription?.take(1)?.uppercase() ?: "?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}