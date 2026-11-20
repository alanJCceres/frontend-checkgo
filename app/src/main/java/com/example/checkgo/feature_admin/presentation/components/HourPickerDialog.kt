package com.example.checkgo.feature_admin.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourPickerDialog(
    initial: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val parts = initial.split(":")
    val pickerState = rememberTimePickerState(
        initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 9,
        initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0,
        is24Hour = true
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 0.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.width(IntrinsicSize.Min)
            //tonalElevation = 6.dp,
            //modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                TimePicker(
                    state = pickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFFf1f1f1), //
                        selectorColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.White,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color(0xFF333333),
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        periodSelectorUnselectedContainerColor = Color(0xFFF1F3F5),
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = Color(0xFF555555),
                        timeSelectorSelectedContainerColor = Color(0xFFf1f1f1), //
                        timeSelectorUnselectedContainerColor = Color(0xFFF1F3F5),
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.primary,
                        timeSelectorUnselectedContentColor = Color(0xFF333333)
                    )
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    TextButton(
                        onClick = {
                            onConfirm(
                                String.format(Locale.US, "%02d:%02d", pickerState.hour, pickerState.minute)
                            )
                        }
                    ) { Text("Aceptar") }
                }
            }
        }
    }
}