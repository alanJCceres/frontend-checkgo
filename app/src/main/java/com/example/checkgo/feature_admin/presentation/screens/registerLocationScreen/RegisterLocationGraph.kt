package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelStoreOwner
import com.example.checkgo.feature_admin.presentation.viewmodel.RegisterLocationUiEvent
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.feature_admin.presentation.components.RegisterLocationStepScaffold
import kotlinx.coroutines.delay

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

             RegisterLocationStepScaffold(
                 currentStep = 1,
                 previousStep = vm.stepperFrom,
                 onStepShown = vm::onStepperShown,
                 overlay = true
             ) {
                 Step1RegLocationScreen(
                     state = state.step1,
                     onChange = vm::onStep1Change,
                     onNext = { if (vm.validateStep1()) navController.navigate(Step2Route) },
                     onMapPositionSelected = vm::onMapPositionSelected
                 )
             }

         }

         composable<Step2Route> { entry ->
             val parent = remember<Any> (entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()

             val lat = state.step1.latitud.toDoubleOrNull()
             val lng = state.step1.longitud.toDoubleOrNull()

             RegisterLocationStepScaffold(
                 currentStep = 2,
                 previousStep = vm.stepperFrom,
                 onStepShown = vm::onStepperShown,
                 onStepClick = { step -> navController.popToStep(step) }
             ){
                 Step2RegLocationScreen(
                     center = LatLng(lat!!, lng!!),
                     state = state.step2,
                     onChange = vm::onStep2Change,
                     onNext = { if (vm.validateStep2()) navController.navigate(Step3Route) },
                     onBack = { navController.popBackStack() }
                 )
             }

         }

         composable<Step3Route> { entry ->
             val parent = remember<Any>(entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()

             RegisterLocationStepScaffold(
                 currentStep = 3,
                 previousStep = vm.stepperFrom,
                 onStepShown = vm::onStepperShown,
                 onStepClick = { step -> navController.popToStep(step) }
             ){
                 Step3RegLocationScreen(
                     state = state.step3,
                     mode = state.step2.toScheduleMode(),
                     onDayChange = vm::onScheduleDayChange,
                     onNext = { if (vm.validateStep3()) navController.navigate(Step4Route) },
                     onBack = { navController.popBackStack() }
                 )
             }

         }

         composable<Step4Route> { entry ->
             val parent = remember<Any>(entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()
             val usersState by vm.usersState.collectAsStateWithLifecycle()
             val saveState by vm.saveState.collectAsStateWithLifecycle()

             var toastMessage by remember { mutableStateOf<String?>(null) }
             var isErrorToast by remember { mutableStateOf(true) }

             LaunchedEffect(toastMessage) {
                 if (toastMessage != null) {
                     delay(4000)
                     toastMessage = null
                 }
             }
             LaunchedEffect(Unit) { vm.loadUsers() }

             LaunchedEffect(Unit) {
                 vm.navigationEvent.collect { event ->
                     when (event) {
                         is RegisterLocationUiEvent.Navigate -> {
                             navController.navigate(event.route) {
                                 popUpTo<RegisterLocationGraph> { inclusive = true }
                             }
                         }
                         is RegisterLocationUiEvent.ShowToast -> {
                             isErrorToast = event.isError
                             toastMessage = event.message
                         }
                     }
                 }
             }
             RegisterLocationStepScaffold(
                 currentStep = 4,
                 previousStep = vm.stepperFrom,
                 onStepShown = vm::onStepperShown,
                 onStepClick = { step -> navController.popToStep(step) }
             ){
                 Box(modifier = Modifier.fillMaxSize()) {
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
                     AnimatedVisibility(
                         visible = toastMessage != null,
                         enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                         exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                         modifier = Modifier.align(Alignment.TopCenter)
                     ) {
                         toastMessage?.let { message ->
                             CustomTopToast(message = message, isError = isErrorToast)
                         }
                     }
                 }
             }


         }

         composable<RegisterLocationSuccessRoute> {
             RegisterLocationSuccessScreen(
                 onFinish = { navController.navigate("homeAdmin") }
             )
         }

     }
}

fun NavController.popToStep(step: Int) {
    when (step) {
        1 -> popBackStack<Step1Route>(inclusive = false)
        2 -> popBackStack<Step2Route>(inclusive = false)
        3 -> popBackStack<Step3Route>(inclusive = false)
    }
}