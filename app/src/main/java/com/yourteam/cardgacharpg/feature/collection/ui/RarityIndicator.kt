package com.yourteam.cardgacharpg.feature.collection.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yourteam.cardgacharpg.core.model.Rarity
import com.yourteam.cardgacharpg.feature.gacha.ui.color
import com.yourteam.cardgacharpg.feature.gacha.ui.label
import kotlin.math.cos
import kotlin.math.sin

// Owner: Person 1 (Leila) — Accessibility-Vorgabe aus dem Projektplan (6.3):
// "Elemente durch Icon UND Form unterscheidbar (nicht nur Farbe)".
// Jede Rarity bekommt zusätzlich zur Farbe eine eindeutige Form:
// Common = Kreis, Rare = Quadrat, Epic = Fünfeck, Legendary = Stern.
// Bewusst mit Canvas gezeichnet statt Icon-Library, um keine neue Dependency zu brauchen.
// Kann von anderen Bereichen (Gacha-Reveal, Arena-Tiles) wiederverwendet werden.
@Composable
fun RarityIndicator(
    rarity: Rarity,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    dotSize: Dp = 14.dp
) {
    val color = rarity.color()
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(dotSize)) {
            drawRarityShape(rarity, color)
        }
        if (showLabel) {
            Spacer(Modifier.width(4.dp))
            Text(
                rarity.label(),
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun DrawScope.drawRarityShape(rarity: Rarity, color: Color) {
    when (rarity) {
        Rarity.COMMON -> drawCircle(color = color)
        Rarity.RARE -> drawRect(color = color)
        Rarity.EPIC -> drawPath(polygonPath(sides = 5), color = color)
        Rarity.LEGENDARY -> drawPath(starPath(points = 5), color = color)
    }
}

private fun DrawScope.polygonPath(sides: Int): Path {
    val radius = size.minDimension / 2f
    val center = Offset(size.width / 2f, size.height / 2f)
    val path = Path()
    for (i in 0 until sides) {
        val angle = -Math.PI / 2 + i * (2 * Math.PI / sides)
        val point = Offset(
            x = center.x + radius * cos(angle).toFloat(),
            y = center.y + radius * sin(angle).toFloat()
        )
        if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    path.close()
    return path
}

private fun DrawScope.starPath(points: Int): Path {
    val outerRadius = size.minDimension / 2f
    val innerRadius = outerRadius / 2.5f
    val center = Offset(size.width / 2f, size.height / 2f)
    val path = Path()
    val totalPoints = points * 2
    for (i in 0 until totalPoints) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = -Math.PI / 2 + i * (Math.PI / points)
        val point = Offset(
            x = center.x + radius * cos(angle).toFloat(),
            y = center.y + radius * sin(angle).toFloat()
        )
        if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    path.close()
    return path
}