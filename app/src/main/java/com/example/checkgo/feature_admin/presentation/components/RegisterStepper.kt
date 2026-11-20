package com.example.checkgo.feature_admin.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.checkgo.core.ui.theme.StyleTextTituloBody
import com.example.checkgo.core.ui.theme.StyleTextTituloHeader

data class StepperStep(val label: String, val title: String)

private val HaloSize = 40.dp
private val InnerSize = 32.dp
@Composable
fun RegisterStepper(
    steps: List<StepperStep>,
    currentStep: Int,                       // 1..steps.size
    modifier: Modifier = Modifier,
    previousStep: Int = currentStep,        // desde dónde arranca la animación
    onStepShown: (Int) -> Unit = {},
    onStepClick: ((Int) -> Unit)? = null
) {
    val total = steps.size
    val target = currentStep.coerceIn(1, total)
    val start = previousStep.coerceIn(1, total)
    val colors = MaterialTheme.colorScheme

    // Progreso continuo 1f..total: lo usan las líneas. Arranca en el step anterior y anima hasta el actual.
    val progress = remember { Animatable(start.toFloat()) }
    // Lo que muestran títulos y nodos: arranca en el step anterior y salta al actual en el primer frame
    var displayStep by remember { mutableIntStateOf(start) }

    LaunchedEffect(target) {
        onStepShown(target)
        displayStep = target
        progress.animateTo(target.toFloat(), tween(durationMillis = 600, easing = FastOutSlowInEasing))
    }

    val trackColor = colors.outlineVariant
    val fillColor = colors.primary

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Paso $target de $total: ${steps[target - 1].title}"
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = target.toFloat(),
                    range = 1f..total.toFloat(),
                    steps = (total - 2).coerceAtLeast(0)
                )
            },
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(
                    text = "Registro de punto de llegada",
                    style = StyleTextTituloBody,
                    textAlign = TextAlign.Center,
                )
            }
//            AnimatedContent(
//                targetState = displayStep,
//                transitionSpec = {
//                    val dir = if (targetState > initialState) 1 else -1
//                    (slideInVertically { it / 2 * dir } + fadeIn()) togetherWith
//                            (slideOutVertically { -it / 2 * dir } + fadeOut())
//                },
//                label = "stepTitle"
//            ) { step ->
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Registro de punto de llegada",
//                        style = StyleTextTituloBody,
//                        textAlign = TextAlign.Center,
//                    )
//                    Text(
//                        text = "Paso $step de $total",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = colors.primary,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }

            // Línea de pasos: las líneas se dibujan detrás, los nodos encima
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val cellWidth = size.width / total
                        val centerY = HaloSize.toPx() / 2f
                        val inset = (InnerSize / 2 + 6.dp).toPx()   // deja un respiro entre nodo y línea
                        val stroke = 3.dp.toPx()
                        for (i in 1 until total) {
                            val startX = cellWidth * (i - 0.5f) + inset
                            val endX = cellWidth * (i + 0.5f) - inset
                            drawLine(trackColor, Offset(startX, centerY), Offset(endX, centerY), stroke, StrokeCap.Round)
                            val fraction = (progress.value - i).coerceIn(0f, 1f)
                            if (fraction > 0f) {
                                drawLine(
                                    fillColor,
                                    Offset(startX, centerY),
                                    Offset(startX + (endX - startX) * fraction, centerY),
                                    stroke,
                                    StrokeCap.Round
                                )
                            }
                        }
                    }
            ) {
                steps.forEachIndexed { index, step ->
                    val number = index + 1
                    StepNode(
                        number = number,
                        label = step.label,
                        displayStep = displayStep,
                        onClick = if (onStepClick != null && number < target) {
                            { onStepClick(number) }
                        } else null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}