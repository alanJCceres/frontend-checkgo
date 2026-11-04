package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

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
                 onBack = { navController.popBackStack() }
             )
         }

         composable<Step2Route> { entry ->
             val parent = remember<Any> (entry) { navController.getBackStackEntry<RegisterLocationGraph>() }
             val vm: RegisterLocationViewModel = hiltViewModel(parent as ViewModelStoreOwner)
             val state by vm.uiState.collectAsStateWithLifecycle()

             Step2RegLocationScreen(
                 state = state.step2,
                 onChange = { vm.onStep2Change(it) },
                 onNext = { if (vm.validateStep2()) navController.navigate("homeAdmin") },
                 onBack = { navController.popBackStack() }
             )
         }

     }
}