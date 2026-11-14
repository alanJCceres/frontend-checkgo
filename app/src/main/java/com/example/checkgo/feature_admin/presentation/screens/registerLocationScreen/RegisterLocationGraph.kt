package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.checkgo.feature_admin.presentation.viewmodel.RegisterLocationViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelStoreOwner
import com.example.checkgo.feature_admin.presentation.viewmodel.RegisterLocationUiEvent
import com.example.checkgo.feature_admin.presentation.viewmodel.SaveState
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable
@Serializable object RegisterLocationGraph
@Serializable object Step1Route
@Serializable object Step2Route
@Serializable object Step3Route
@Serializable object Step4Route
fun NavGraphBuilder.registerLocationGraph(navController: NavHostController)
 {
     navigation<RegisterLocationGraph>(startDestination = Step1Route){

         composable<Step1Route> { entry ->
             val parent = remember<Any> (entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()

             Step1RegLocationScreen(
                 state = state.step1,
                 onChange = vm::onStep1Change,
                 onNext = { if (vm.validateStep1()) navController.navigate(Step2Route) },
                 onMapPositionSelected = vm::onMapPositionSelected
             )
         }

         composable<Step2Route> { entry ->
             val parent = remember<Any> (entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()

             val lat = state.step1.latitud.toDoubleOrNull()
             val lng = state.step1.longitud.toDoubleOrNull()

             Step2RegLocationScreen(
                 center = LatLng(lat!!, lng!!),
                 state = state.step2,
                 onChange = vm::onStep2Change,
                 onNext = { if (vm.validateStep2()) navController.navigate(Step3Route) },
                 onBack = { navController.popBackStack() }
             )
         }

         composable<Step3Route> { entry ->
             val parent = remember<Any>(entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()
             Step3RegLocationScreen(
                 state = state.step3,
                 mode = state.step2.toScheduleMode(),
                 onDayChange = vm::onScheduleDayChange,
                 onNext = { if (vm.validateStep3()) navController.navigate(Step4Route) },
                 onBack = { navController.popBackStack() }
             )
         }

         composable<Step4Route> { entry ->
             val parent = remember<Any>(entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()
             val usersState by vm.usersState.collectAsStateWithLifecycle()
             val saveState by vm.saveState.collectAsStateWithLifecycle()

             LaunchedEffect(Unit) { vm.loadUsers() }

             LaunchedEffect(Unit) {
                 vm.navigationEvent.collect { event ->
                     when (event) {
                         is RegisterLocationUiEvent.Navigate -> {
                             navController.navigate(event.route) {
                                 popUpTo<RegisterLocationGraph> { inclusive = true }
                             }
                         }
                     }
                 }
             }
             Step4RegLocationScreen(
                 usersState = usersState,
                 state = state.step4,
                 saveState = saveState,
                 onUserSelectionChange = vm::onUserSelectionChange,
                 onRetry = { vm.loadUsers(force = true) },
                 onSave = vm::save,
                 onBack = { navController.popBackStack() },
                 onRegisterNewUser = { navController.navigate("registerUser") }
             )
         }

         composable<RegisterLocationSuccessRoute> {
             RegisterLocationSuccessScreen(
                 onFinish = { navController.navigate("homeAdmin") }
             )
         }

     }
}