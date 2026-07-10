package com.yourteam.cardgacharpg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaScreen
import com.yourteam.cardgacharpg.feature.arena.ui.HomeScreen
import com.yourteam.cardgacharpg.feature.battle.ui.FormationEditorScreen
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignMapScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CardDetailScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaScreen

object Routes {
    const val HOME = "home"
    const val COLLECTION = "collection"
    const val CARD_DETAIL_ARG = "cardId"
    const val CARD_DETAIL = "card_detail/{$CARD_DETAIL_ARG}"
    fun cardDetail(cardId: Int) = "card_detail/$cardId"
    const val GACHA = "gacha"
    const val FORMATION = "formation"
    const val CAMPAIGN = "campaign"
    const val ARENA = "arena"
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenCollection = { navController.navigate(Routes.COLLECTION) },
                onOpenGacha = { navController.navigate(Routes.GACHA) },
                onOpenFormation = { navController.navigate(Routes.FORMATION) },
                onOpenCampaign = { navController.navigate(Routes.CAMPAIGN) },
                onOpenArena = { navController.navigate(Routes.ARENA) }
            )
        }

        composable(Routes.COLLECTION) {
            CollectionScreen(
                onCardClick = { card -> navController.navigate(Routes.cardDetail(card.id)) },
                onOpenGacha = { navController.navigate(Routes.GACHA) }
            )
        }

        composable(
            route = Routes.CARD_DETAIL,
            arguments = listOf(navArgument(Routes.CARD_DETAIL_ARG) { type = NavType.IntType })
        ) {
            CardDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.GACHA) {
            GachaScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.FORMATION) {
            FormationEditorScreen()
        }

        composable(Routes.CAMPAIGN) {
            CampaignMapScreen()
        }

        composable(Routes.ARENA) {
            ArenaScreen(onBack = { navController.popBackStack() })
        }
    }
}