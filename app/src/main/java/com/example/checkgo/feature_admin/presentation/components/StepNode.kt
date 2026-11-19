package com.example.checkgo.feature_admin.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

val RegisterLocationSteps = listOf(
    StepperStep("Ubicación", "Ubica el punto en el mapa"),
    StepperStep("Reglas", "Define el radio y el tipo de control"),
    StepperStep("Horarios", "Configura los horarios por día"),
    StepperStep("Usuarios", "Selecciona a los usuarios")
)

private enum class NodeState { Done, Current, Upcoming }
private val HaloSize = 40.dp
private val InnerSize = 32.dp

@Composable
fun StepNode(
    number: Int,
    label: String,
    displayStep: Int,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val state = when {
        number < displayStep -> NodeState.Done
        number == displayStep -> NodeState.Current
        else -> NodeState.Upcoming
    }
    val active = number <= displayStep

    // Al activarse espera a que la línea llegue; al desactivarse reacciona de inmediato
    val colorSpec = tween<androidx.compose.ui.graphics.Color>(300, delayMillis = if (active) 300 else 0)
    val fill by animateColorAsState(if (active) colors.primary else Color.White, colorSpec, label = "fill")
    val border by animateColorAsState(if (active) colors.primary else colors.outline, colorSpec, label = "border")
    val content by animateColorAsState(
        if (active) colors.onPrimary else colors.onSurfaceVariant, colorSpec, label = "content"
    )
    val labelColor by animateColorAsState(
        if (state == NodeState.Upcoming) colors.onSurfaceVariant else colors.onSurface,
        colorSpec,
        label = "labelColor"
    )
    val halo by animateFloatAsState(
        targetValue = if (state == NodeState.Current) 0.18f else 0f,
        animationSpec = tween(300, delayMillis = if (state == NodeState.Current) 300 else 0),
        label = "halo"
    )

    val stateText = when (state) {
        NodeState.Done -> "Completado"
        NodeState.Current -> "Paso actual"
        NodeState.Upcoming -> "Pendiente"
    }

    Column(
        modifier = modifier
            .then(
                if (onClick != null) Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(role = Role.Button, onClickLabel = "Volver a $label", onClick = onClick)
                else Modifier
            )
            .semantics(mergeDescendants = true) {
                contentDescription = "Paso $number, $label"
                stateDescription = stateText
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(HaloSize)
                .drawBehind { drawCircle(colors.primary.copy(alpha = halo)) },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(InnerSize)
                    .drawBehind {
                        drawCircle(fill)
                        drawCircle(
                            color = border,
                            radius = size.minDimension / 2 - 1.dp.toPx(),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = state == NodeState.Done, label = "nodeContent") { done ->
                    if (done) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = content,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            text = "$number",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = content
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = labelColor,
            fontWeight = if (state == NodeState.Current) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}