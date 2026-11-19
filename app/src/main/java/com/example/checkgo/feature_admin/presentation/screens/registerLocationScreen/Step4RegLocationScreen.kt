package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_admin.presentation.components.CheckCardUser
import com.example.checkgo.feature_admin.presentation.viewmodel.SaveState
import com.example.checkgo.feature_admin.presentation.viewmodel.Step4State
import com.example.checkgo.feature_admin.presentation.viewmodel.Step4UsersUiState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import com.example.checkgo.core.ui.components.EmptyStateView
import com.example.checkgo.core.ui.theme.StyleTextBodySmall
import com.example.checkgo.core.ui.theme.StyleTextTituloBody


@Composable
fun Step4RegLocationScreen(
    usersState: Step4UsersUiState,
    state: Step4State,
    saveState: SaveState,
    onUserSelectionChange: (userId: String, selected: Boolean) -> Unit,
    onRetry: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onRegisterNewUser: ()->Unit,
    modifier: Modifier = Modifier
) {
    val selectedIds = remember(state.usersSelected) { state.usersSelected.toSet() }
    val isSaving = saveState is SaveState.Saving

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Selecciona los usuarios",
            style = StyleTextTituloBody
        )
        Text(
            text = "${state.usersSelected.size} seleccionados",
            style = StyleTextBodySmall,
            color = Color.Gray
        )
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) {
            when (usersState) {
                Step4UsersUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                is Step4UsersUiState.Error -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(usersState.message, color = Color.Red)
                    OutlinedButton(onClick = onRetry) { Text("Reintentar") }
                }
                is Step4UsersUiState.Empty -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        EmptyStateView(
                            title = "Sin usuarios",
                            description = "Actualmente no hay usuarios registrados."
                        )
                        OutlinedButton(onClick = onRegisterNewUser) { Text("Agregar usuario") }
                    }
                }
                is Step4UsersUiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(usersState.users, key = { it.publicId }) { user ->
                            CheckCardUser(
                                user = user,
                                modifier = Modifier.fillMaxWidth(),
                                selected = user.publicId in selectedIds,
                                onSelectedChange = { onUserSelectionChange(user.publicId, it) }
                            )
                        }
                    }
                }
            }
        }

        state.usersError?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
        (saveState as? SaveState.Error)?.let {
            Spacer(Modifier.height(8.dp))
            Text(it.message, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButton(
                text = "Atrás",
                onClick = onBack,
                enabled = !isSaving,
                modifier = Modifier.weight(1f),
                isPrimary = false
            )
            CustomButton(
                text = "Guardar",
                onClick = onSave,
                loadingText = "Cargando...",
                isLoading = isSaving,
                enabled = !isSaving,
                modifier = Modifier.weight(1f)
            )
        }
    }
}