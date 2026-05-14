package com.karunadavanya.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karunadavanya.data.AppContainer
import com.karunadavanya.presentation.localization.ProvideAppLanguage
import com.karunadavanya.presentation.screens.*

private object Route {
    const val Splash = "splash"
    const val Home = "home"
    const val Wildlife = "wildlife"
    const val WildlifeDetail = "wildlife/{wildlifeId}"
    const val Alerts = "alerts"
    const val AlertDetail = "alerts/{alertId}"
    const val Report = "report"
}

@Composable
fun KarunadaVanyaApp(container: AppContainer) {
    val navController = rememberNavController()

    ProvideAppLanguage {
        NavHost(navController = navController, startDestination = Route.Splash) {
        composable(Route.Splash) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Home) {
            HomeScreen(
                onExploreWildlife = { navController.navigate(Route.Wildlife) },
                onOpenAlerts = { navController.navigate(Route.Alerts) },
                onOpenReport = { navController.navigate(Route.Report) }
            )
        }
        composable(Route.Wildlife) {
            WildlifeListScreen(
                onBack = navController::navigateUp,
                onHome = { navController.navigate(Route.Home) },
                onAlerts = { navController.navigate(Route.Alerts) },
                onReport = { navController.navigate(Route.Report) },
                onWildlifeClick = { id: Int -> navController.navigate("wildlife/$id") }
            )
        }
        composable(
            route = Route.WildlifeDetail,
            arguments = listOf(navArgument("wildlifeId") { type = NavType.IntType })
        ) { entry ->
            val wildlifeId: Int = checkNotNull(entry.arguments?.getInt("wildlifeId"))
            WildlifeDetailScreen(
                wildlifeId = wildlifeId,
                onBack = navController::navigateUp
            )
        }
        composable(Route.Alerts) {
            AlertsScreen(
                onHome = { navController.navigate(Route.Home) },
                onExplore = { navController.navigate(Route.Wildlife) },
                onReport = { navController.navigate(Route.Report) },
                onAlertClick = { alertId: String -> navController.navigate("alerts/$alertId") }
            )
        }
        composable(Route.Report) {
            val reportViewModel: ReportViewModel = viewModel(
                factory = SimpleViewModelFactory {
                    ReportViewModel(container.sightingReportRepository)
                }
            )
            ReportScreen(
                viewModel = reportViewModel,
                onHome = { navController.navigate(Route.Home) },
                onExplore = { navController.navigate(Route.Wildlife) },
                onAlerts = { navController.navigate(Route.Alerts) }
            )
        }
        composable(
            route = Route.AlertDetail,
            arguments = listOf(navArgument("alertId") { type = NavType.StringType })
        ) { entry ->
            val alertId: String = checkNotNull(entry.arguments?.getString("alertId"))
            AlertDetailScreen(
                alertId = alertId,
                onBack = navController::navigateUp
            )
        }
        }
    }
}
